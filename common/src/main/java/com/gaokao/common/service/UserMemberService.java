package com.gaokao.common.service;


import com.gaokao.common.enums.VeryCodeType;
import com.gaokao.common.meta.vo.user.MemberUpdateParams;
import com.gaokao.common.meta.vo.user.RegParams;
import com.gaokao.common.meta.vo.user.UserMemberVO;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserMemberService extends UserDetailsService {
    /**
     * 注册
     *
     * @param regParams 注册参数
     * @return 注册成功返回用户Id
     */
    Long reg(RegParams regParams);
    /**
     * 发送验证码
     *
     * @param type  类型
     * @param phone 手机号码
     * @return 是否发送成功
     */
    String sendVerifyCode(VeryCodeType type, String phone);

    /**
     * 查询所有用户
     * @param keyword
     * @param page
     * @param size
     * @return
     */
    Page<UserMemberVO> list(String keyword, Integer page, Integer size);


    /**
     * 修改用户信息
     *
     *
     * @param userId 用户Id
     *
     * @return 记录Id
     */


    Long update (Long userId, MemberUpdateParams params);

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    UserMemberVO getInfo(Long id);

    /**
     * 修改密码
     * @param id     记录Id
     * @param originPwd 之前的密码
     * @param newPwd 新密码
     * @return -1用户不存在 0旧密码错误
     */
    int changePwd(long id, String originPwd, String newPwd);

    /**
     * 锁定用户
     * @param userId 锁定用户id
     * @return 成功则返回被锁定用户的id，否则返回-1
     */
    Long lock(Long userId);

    /**
     * 解锁用户
     * @param userId 解锁用户id
     * @return 成功则返回被解锁的用户id，否则返回-1
     */
    Long unlock(Long userId);

}
