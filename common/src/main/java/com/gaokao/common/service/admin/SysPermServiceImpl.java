package com.gaokao.common.service.admin;

import com.gaokao.common.constants.AppConstant;
import com.gaokao.common.dao.SysPermDao;
import com.gaokao.common.dao.SysRolePermDao;
import com.gaokao.common.dao.SysUserRoleDao;
import com.gaokao.common.exceptions.BusinessException;
import com.gaokao.common.meta.po.SysPerm;
import com.gaokao.common.meta.po.SysRolePerm;
import com.gaokao.common.meta.po.SysUserRole;
import com.gaokao.common.meta.vo.admin.PermTreeVO;
import com.gaokao.common.meta.vo.admin.SysPermSaveParams;
import com.gaokao.common.utils.UserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author attack204
 * date:  2021/7/19
 * email: 757394026@qq.com
 */
@Slf4j
@Service
public class SysPermServiceImpl implements SysPermService {

    @Autowired
    private SysPermDao sysPermDao;

    @Autowired
    private SysUserRoleDao sysUserRoleDao;

    @Autowired
    private SysRolePermDao sysRolePermDao;

    @Override
    public Long create(SysPermSaveParams params) {
        if(sysPermDao.findByCode(params.getCode()) != null) {
            throw new BusinessException("权限编码已存在");
        }
        SysPerm sysPerm = new SysPerm();
        BeanUtils.copyProperties(params, sysPerm);
        long currentTime = System.currentTimeMillis();
        sysPerm.setUpdater(UserUtils.getUserName());
        sysPerm.setCreateTime(currentTime);
        sysPerm.setUpdateTime(currentTime);
        sysPerm.setCreator(UserUtils.getUserName());
        sysPermDao.save(sysPerm);
        return sysPerm.getId();
    }


    /**
     * 更新用户信息
     * 如果该id已经存在，则进行更新
     * 否则新建
     *
     * @param id     记录Id
     * @param params 参数信息
     * @return 若成功则返回编号，否则返回-1
     */
    @Override
    public Long update(Long id, SysPermSaveParams params) {
        List<SysPerm> perms = sysPermDao.findAllById(id);
        SysPerm tmp = sysPermDao.findByCode(params.getCode());
        if((tmp != null) && (!tmp.getId().equals(id))) {
            throw new BusinessException("权限编码已存在");
        }
        if (perms.size() == 1) {
            //更新用户信息
            SysPerm tempPerms;
            tempPerms = perms.get(0);
            tempPerms.setId(params.getId());
            tempPerms.setPid(params.getPid());
            tempPerms.setCode(params.getCode());
            tempPerms.setName(params.getName());
            tempPerms.setUpdateTime(System.currentTimeMillis());
            tempPerms.setUpdater(UserUtils.getUserName());
            sysPermDao.save(tempPerms);
            return tempPerms.getId();
        } else {
            return -1L;
        }
    }

    private Set<Long> getPerms(Long userId) {
        List<SysUserRole> userRoles = sysUserRoleDao.findAllByUserId(userId);
        Set<Long> permIds = new HashSet<>();
        userRoles.forEach(item -> {
            List<SysRolePerm> rolePerms = sysRolePermDao.findAllByRoleId(item.getRoleId());
            rolePerms.forEach(rolePerm -> permIds.add(rolePerm.getPermId()));
        });
        return permIds;
    }

    @Override
    public List<PermTreeVO> list(Long userId) {
        Set<Long> permIds = getPerms(userId);
        return buildPermTree(AppConstant.BASE_PARENT_ID, permIds, UserUtils.isPlatformUser());
    }

    private List<PermTreeVO> buildPermTree(Long pid, Set<Long> permIds, boolean isSuperAdmin) {
        List<SysPerm> perms = sysPermDao.findAllByPid(pid);
        if (perms == null || perms.isEmpty()) {
            return null;
        }
        List<PermTreeVO> permTreeVOS = new ArrayList<>(perms.size());
        for (SysPerm perm : perms) {
            if (isSuperAdmin || permIds.contains(perm.getId())) {
                PermTreeVO permTreeVO = new PermTreeVO();
                BeanUtils.copyProperties(perm, permTreeVO);
                permTreeVO.setChildren(buildPermTree(perm.getId(), permIds, isSuperAdmin));
                permTreeVOS.add(permTreeVO);
            }
        }
        return permTreeVOS;
    }

    @Override
    public Long deleteById(Long permId) {
        List<SysPerm> perms = sysPermDao.findAllById(permId);

        if (perms.size() != 1) {
            //未找到指定id或者id个数>1
            return -1L;
        } else {
            sysPermDao.deleteById(permId);
            return permId;
        }
    }
}
