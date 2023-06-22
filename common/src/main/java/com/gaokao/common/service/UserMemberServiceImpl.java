package com.gaokao.common.service;


import com.alibaba.fastjson.JSON;
import com.gaokao.common.dao.UserMemberDao;
import com.gaokao.common.enums.UserMemberStatus;
import com.gaokao.common.enums.VeryCodeType;
import com.gaokao.common.exceptions.BusinessException;
import com.gaokao.common.meta.bo.JwtUser;
import com.gaokao.common.meta.po.UserMember;
import com.gaokao.common.meta.vo.user.MemberUpdateParams;
import com.gaokao.common.meta.vo.user.RegParams;
import com.gaokao.common.meta.vo.user.UserMemberVO;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 * @author wyc
 * date:  2021/8/16
 */
@Slf4j
@Service
public class UserMemberServiceImpl implements UserMemberService{
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserMemberDao userMemberDao;

    @Autowired
    private VerifyCodeService verifyCodeService;
    @Override
    public String sendVerifyCode(VeryCodeType type, String phone) {
        return verifyCodeService.sendVeryCode(type, phone);
    }

    // 好像是登录用的..?
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserMember userMember = userMemberDao.findUserMemberByPhone(s);
        if (userMember != null) {
            return new JwtUser(userMember.getPhone(), userMember.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList("HasLoggedIn"), userMember.getId());
        }
        userMember = userMemberDao.findByWxOpenId(s);
        if (userMember == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return new JwtUser(userMember.getWxOpenId(), userMember.getWxSKey(),
                AuthorityUtils.commaSeparatedStringToAuthorityList("HasLoggedIn"), userMember.getId());
    }

    // 新增用户
    @Override
    public Long reg(RegParams regParams) {
        UserMember userMember = userMemberDao.findUserMemberByPhone(regParams.getPhone());
        if (userMember != null) {
            throw new BusinessException("用户已经存在");
        }

        userMember = new UserMember();
        userMember.setProvinceRank((long)Math.toIntExact(regParams.getProvinceRank()));
        userMember.setScore(regParams.getScore());
        userMember.setNickname(regParams.getNickname());
        userMember.setPhone(regParams.getPhone());
        userMember.setSubject(JSON.toJSONString(regParams.getSubject()));
        userMember.setStatus(UserMemberStatus.NORMAL.getCode());
        userMember.setPassword(passwordEncoder.encode(regParams.getPassword()));

        long currentTime = System.currentTimeMillis();
        userMember.setCreateTime(currentTime);
        userMember.setUpdateTime(currentTime);
        return userMemberDao.save(userMember).getId();
    }


    // 获取列表和搜索的接口对应
    @Override
    public Page<UserMemberVO> list(String keyword, Integer page, Integer size) {

          List<UserMember> phones = userMemberDao.findByPhoneContaining(keyword, PageRequest.of(page - 1, size));
          List<UserMemberVO> phoneVOS = new ArrayList<>(phones.size());
          phones.forEach(userMember -> {
              UserMemberVO userMemberVO = new UserMemberVO();
            BeanUtils.copyProperties(userMember, userMemberVO);
            phoneVOS.add(userMemberVO);
        });
        return new PageImpl<>(phoneVOS, PageRequest.of(page - 1, size), phoneVOS.size());
    }

    @Override
    public Long update(Long userId, MemberUpdateParams params) {
        UserMember userMember = userMemberDao.findById(userId).orElse(null);
        if (userMember == null) {
            throw new BusinessException("记录不存在");
        }
        BeanUtils.copyProperties(params, userMember);
        userMember.setCreateTime(System.currentTimeMillis());
        return userMemberDao.save(userMember).getId();
    }

    @Override
    public int changePwd(long id, String originPwd, String newPwd) {

        Optional<UserMember> optionalUser = userMemberDao.findById(id);
        UserMember user = optionalUser.orElse(null);
        if (user == null) {
            return -1;
        }
        //originPwd = passwordEncoder.encode(originPwd);
        //passwordEncoder每次的salt都是不一样的，因此无法直接加密，用字符串比较（即使是相同的字符串，加密后的结果也不相同
        if (passwordEncoder.matches(originPwd, user.getPassword())) {
            newPwd = passwordEncoder.encode(newPwd);
            user.setPassword(newPwd);
            user.setUpdateTime(System.currentTimeMillis());
            userMemberDao.save(user);
            return 1;
        }
        return 0;
    }

    @Override
    public Long lock(Long userId) {
        Optional<UserMember> optionalUser = userMemberDao.findById(userId);
        UserMember userMember = optionalUser.orElse(null);
        if (userMember == null) {
            return -1L;
        } else {
            userMember.setStatus(UserMemberStatus.LOCKED.getCode());
            userMemberDao.save(userMember);
            return userId;
        }
    }

    @Override
    public Long unlock(Long userId) {
        Optional<UserMember> optionalUser = userMemberDao.findById(userId);
        UserMember userMember = optionalUser.orElse(null);
        if (userMember == null) {
            return -1L;
        } else {
            userMember.setStatus(UserMemberStatus.NORMAL.getCode());
            userMember.setUpdateTime(System.currentTimeMillis());
            userMemberDao.save(userMember);
            return userId;
        }
    }

    @Override
    public UserMemberVO getInfo(Long id) {
        UserMember userMember = userMemberDao.findById(id).orElse(null);

        UserMemberVO userMemberVO = new UserMemberVO();
        if (userMember == null) {
            throw new BusinessException("用户不存在");
        }
       else {
            BeanUtils.copyProperties(userMember, userMemberVO);
        }
       userMemberVO.setSubject(JSON.parseArray(userMember.getSubject(), Long.class));
        return userMemberVO;

    }

}
