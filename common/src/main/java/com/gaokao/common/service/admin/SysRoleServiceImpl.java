package com.gaokao.common.service.admin;

import com.gaokao.common.dao.SysPermDao;
import com.gaokao.common.dao.SysRoleDao;
import com.gaokao.common.dao.SysRolePermDao;
import com.gaokao.common.exceptions.BusinessException;
import com.gaokao.common.meta.po.SysRole;
import com.gaokao.common.meta.po.SysRolePerm;
import com.gaokao.common.meta.vo.admin.SysRoleSaveParams;
import com.gaokao.common.meta.vo.admin.SysRoleVO;
import com.gaokao.common.meta.vo.common.NameValuePair;
import com.gaokao.common.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
@Slf4j
@Service
public class SysRoleServiceImpl implements SysRoleService{
    //TODO:后端权限控制暂时先不做，后续再做

    @Autowired
    private SysRoleDao sysRoleDao;
    @Autowired
    private SysRolePermDao sysRolePermDao;

    @Autowired
    private SysPermDao sysPermDao;

    @Override
    public Long insert(SysRoleSaveParams params, Long corpId, String operator) {
        SysRole origin = sysRoleDao.findAllByName(params.getName());
        if (origin != null) {
            throw new BusinessException("角色名称已存在！");
        }
        Long currentTime = System.currentTimeMillis();
        SysRole sysRole = new SysRole();
        BeanUtils.copyProperties(params, sysRole);
        sysRole.setCorp(corpId);
        sysRole.setCreator(operator);
        sysRole.setCreateTime(currentTime);
        sysRole.setUpdater(operator);
        sysRole.setUpdateTime(currentTime);
        sysRoleDao.save(sysRole);

        allocPerm(sysRole.getId(), params.getPermIds());
        return sysRole.getId();
    }

    @Override
    public Long update(Long id, SysRoleSaveParams params) {
        Optional<SysRole> result = sysRoleDao.findById(id);
        SysRole sysRole = result.orElse(null);
        if (sysRole == null) {
            throw new BusinessException("角色不存在");
        } else {
            BeanUtils.copyProperties(params, sysRole);
            sysRole.setUpdateTime(System.currentTimeMillis());
            sysRole.setUpdater(UserUtils.getUserName());
            sysRoleDao.save(sysRole);
            allocPerm(sysRole.getId(), params.getPermIds());
            return id;
        }
    }

    @Override
    public Long delete(Long id) {
        Optional<SysRole> result = sysRoleDao.findById(id);
        SysRole sysRole = result.orElse(null);
        if (sysRole == null) {
            return -1L;
        } else {
            sysRoleDao.deleteById(id);
            return id;
        }
    }

    @Override
    public Page<SysRoleVO> list(String keyword, Long corpId, Long operatorCorpId, Integer page, Integer size) {
        Page<SysRole> sysRoles;
        //超管可以获取全部角色信息
        if (operatorCorpId == -1) {
            sysRoles = sysRoleDao.findAllByNameContaining(keyword, PageRequest.of(page - 1, size));
        } else if (operatorCorpId == 0) {
            //普通管理员只能查询到商家的角色
            Long shopCorpId = 0L; //在tb_sys_role表中,将未使用的字段corp作为标识符使用，为0代表商家,-1代表管理员
            sysRoles = sysRoleDao.findAllByNameContainingAndCorp(keyword, shopCorpId, PageRequest.of(page - 1, size));
        }else{
            throw new BusinessException("无权限访问！");
        }
        List<SysRoleVO> sysRoleVOS = new ArrayList<>(sysRoles.getNumberOfElements());
        sysRoles.forEach(item -> {
            SysRoleVO sysRoleVO = new SysRoleVO();
            BeanUtils.copyProperties(item, sysRoleVO);
            Set<Long> permIds = new HashSet<>();
            List<SysRolePerm> rolePerms = sysRolePermDao.findAllByRoleId(item.getId());
            for (SysRolePerm rolePerm : rolePerms) {
                permIds.add(rolePerm.getPermId());
            }
            sysRoleVO.setPermIds(permIds);
            sysRoleVOS.add(sysRoleVO);
        });

        return new PageImpl<>(sysRoleVOS, PageRequest.of(page - 1, size), sysRoles.getTotalElements());

    }

    private void allocPerm(Long roleId, Set<Long> permIds) {
        sysRolePermDao.deleteAllByRoleId(roleId);
        if (permIds != null) {
            for (Long permId : permIds) {
                SysRolePerm sysRolePerm = new SysRolePerm();
                sysRolePerm.setPermId(permId);
                sysRolePerm.setRoleId(roleId);
                sysRolePermDao.save(sysRolePerm);
            }
        }
    }

    @Override
    public Set<String> listAllPermsByRoleIds(Set<Long> roleIds) {
        if (roleIds == null) {
            return Collections.emptySet();
        }
        Set<String> permCodes = new HashSet<>();
        for (Long roleId : roleIds) {
            List<SysRolePerm> rolePerms = sysRolePermDao.findAllByRoleId(roleId);
            for (SysRolePerm rolePerm : rolePerms) {
                sysPermDao.findById(rolePerm.getPermId()).ifPresent(sysPerm -> permCodes.add(sysPerm.getCode()));
            }

        }
        return permCodes;
    }

    @Override
    public List<NameValuePair> list(Long corpId) {
        List<SysRole> roles = sysRoleDao.findAllByCorp(corpId);
        List<NameValuePair> roleVos = new ArrayList<>(roles.size());
        roles.forEach(item -> {
            roleVos.add(new NameValuePair(item.getName(), item.getId()));
        });
        return roleVos;
    }
}
