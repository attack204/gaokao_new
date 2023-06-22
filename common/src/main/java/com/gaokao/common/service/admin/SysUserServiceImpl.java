package com.gaokao.common.service.admin;

import com.gaokao.common.constants.AppConstant;
import com.gaokao.common.dao.SysRoleDao;
import com.gaokao.common.dao.SysUserDao;
import com.gaokao.common.dao.SysUserRoleDao;
import com.gaokao.common.exceptions.BusinessException;
import com.gaokao.common.meta.bo.JwtUser;
import com.gaokao.common.meta.bo.UserInfo;
import com.gaokao.common.meta.po.SysUser;
import com.gaokao.common.meta.po.SysUserRole;
import com.gaokao.common.meta.vo.admin.SysUserCreateParams;
import com.gaokao.common.meta.vo.admin.SysUserUpdateParams;
import com.gaokao.common.meta.vo.admin.UserVO;
import com.gaokao.common.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
@Slf4j
@Service
public class SysUserServiceImpl implements SysUserService  {
    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Autowired
    private SysRoleDao sysRoleDao;

    @Autowired
    private SysRoleService sysRoleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Long create(SysUserCreateParams params) {
        SysUser userMember = sysUserDao.findSysUserByUsername(params.getUsername());
        if (userMember != null) {
            throw new BusinessException("用户已经存在");
        }
        if (sysUserDao.findByPhone(params.getPhone()) != null) {
            throw new BusinessException("手机号已存在");
        }
        userMember = new SysUser();
        userMember.setCorp(params.getCorp());
        userMember.setUsername(params.getUsername());
        userMember.setPhone(params.getPhone());
        userMember.setPassword(passwordEncoder.encode(params.getPassword()));
        long currentTime = System.currentTimeMillis();
        userMember.setCreator(UserUtils.getUserName());
        userMember.setCreateTime(currentTime);
        userMember.setUpdater(UserUtils.getUserName());
        userMember.setUpdateTime(currentTime);
        sysUserDao.save(userMember);
        allocRoles(userMember.getId(), params.getRoleIds());
        return userMember.getId();
    }

    @Override
    public boolean allocRoles(Long userId, Set<Long> roleIds) {
        sysUserRoleDao.deleteAllByUserId(userId);
        if (roleIds != null) {
            for (Long roleId : roleIds) {
                SysUserRole sysUserRole = new SysUserRole();
                sysUserRole.setUserId(userId);
                sysUserRole.setRoleId(roleId);
                sysUserRoleDao.save(sysUserRole);
            }
        }
        return true;
    }

    @Override
    public UserInfo getInfo(Long userId) {
        SysUser sysUser = sysUserDao.findById(userId).orElse(null);
        if (sysUser == null) {
            throw new BusinessException("用户不存在");
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setCorp(sysUser.getCorp());
        userInfo.setName(sysUser.getUsername());
        List<SysUserRole> userRoles = sysUserRoleDao.findAllByUserId(userId);
        Set<String> roles = new HashSet<>(userRoles.size());
        Set<Long> roleIds = new HashSet<>(userRoles.size());
        for (SysUserRole userRole : userRoles) {
            sysRoleDao.findById(userRole.getRoleId()).ifPresent(sysRole -> {
                roles.add(sysRole.getName());
                roleIds.add(sysRole.getId());
            });
        }
        userInfo.setRoles(roles);
        userInfo.setPermissions(sysRoleService.listAllPermsByRoleIds(roleIds));
        return userInfo;
    }

    @Override
    public SysUser getByUsername(String username) {
        return sysUserDao.findSysUserByUsername(username);
    }

    @Override
    public Page<UserVO> list(String keyword, Integer page, Integer size, Long corpId) {
        Page<SysUser> users;

        users = sysUserDao.findByUsernameContaining(keyword, PageRequest.of(page - 1, size));

        List<UserVO> userVOS = new ArrayList<>(users.getContent().size());
        for (SysUser user : users) {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(user, userVO);
            if (AppConstant.ADMIN_CORP.equals(userVO.getCorp()) || AppConstant.GENERAL_ADMIN_CORP.equals(userVO.getCorp())) {
                userVO.setCorpName(AppConstant.PLATFORM_NAME);
            } else {
                userVO.setCorpName(AppConstant.PLATFORM_NAME);
            }
            List<SysUserRole> userRoles = sysUserRoleDao.findAllByUserId(user.getId());
            Set<String> roles = new HashSet<>(userRoles.size());
            Set<Long> roleIds = new HashSet<>(userRoles.size());
            for (SysUserRole userRole : userRoles) {
                roleIds.add(userRole.getRoleId());
                sysRoleDao.findById(userRole.getRoleId()).ifPresent(sysRole -> {
                    roles.add(sysRole.getName());
                });
            }
            userVO.setRoleIds(roleIds);
            userVO.setRoles(roles);
            userVOS.add(userVO);
        }
        return new PageImpl<>(userVOS, users.getPageable(), users.getTotalElements());
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser userMember = sysUserDao.findSysUserByUsername(username);
        if (userMember == null) {
            userMember = sysUserDao.findByPhone(username);
        }
        if (userMember == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        JwtUser jwtUser = new JwtUser(userMember.getUsername(), userMember.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("HasLoggedIn"), userMember.getId());
        jwtUser.setCorp(userMember.getCorp());
        jwtUser.setPerms(getPerms(userMember.getId()));
        return jwtUser;
    }

    private Map<String, Set<String>> getPerms(Long userId) {
        List<SysUserRole> userRoles = sysUserRoleDao.findAllByUserId(userId);
        Set<Long> roleIds = new HashSet<>(userRoles.size());
        for (SysUserRole userRole : userRoles) {
            roleIds.add(userRole.getRoleId());
        }
        Set<String> resAndPerms = sysRoleService.listAllPermsByRoleIds(roleIds);
        Map<String, Set<String>> resAndPermsMap = new HashMap<>();
        for (String resAndPerm : resAndPerms) {
            String[] resAndPermArray = resAndPerm.split(AppConstant.PERM_SPLITTER);
            if (resAndPermArray.length == 2) {
                Set<String> permCodes = resAndPermsMap.computeIfAbsent(resAndPermArray[0], k -> new HashSet<>());
                permCodes.add(resAndPermArray[1]);
            }
        }
        return resAndPermsMap;
    }

    @Override
    public int changePwd(long id, String originPwd, String newPwd) {
        Optional<SysUser> optionalUser = sysUserDao.findById(id);
        SysUser user = optionalUser.orElse(null);
        if (user == null) {
            return -1;
        }
        //originPwd = passwordEncoder.encode(originPwd);
        //passwordEncoder每次的salt都是不一样的，因此无法直接加密，用字符串比较（即使是相同的字符串，加密后的结果也不相同
        if (passwordEncoder.matches(originPwd, user.getPassword())) {
            newPwd = passwordEncoder.encode(newPwd);
            user.setPassword(newPwd);
            user.setUpdateTime(System.currentTimeMillis());
            sysUserDao.save(user);
            return 1;
        }
        return 0;
    }

    @Override
    public Long deleteById(Long id) {
        List<SysUser> users = sysUserDao.findAllById(id);
        if (users.size() != 1) {
            return -1L;
        } else {
            sysUserDao.deleteById(id);
            return id;
        }
    }

    @Override
    public Long updateById(SysUserUpdateParams params, Long id) {
        SysUser user = sysUserDao.findById(id).orElse(null);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        if (sysUserDao.findByPhone(params.getPhone()).getId() != id) {
            throw new BusinessException("手机号已存在");
        }
        user.setCorp(params.getCorp());
        user.setUsername(params.getUsername());
        user.setPhone(params.getPhone());
        user.setUpdateTime(System.currentTimeMillis());
        user.setUpdater(UserUtils.getUserName());
        sysUserDao.save(user);
        allocRoles(user.getId(), params.getRoleIds());
        return user.getId();
    }

    @Override
    public SysUser getById(Long id) {
        return sysUserDao.findSysUserById(id);
    }
}
