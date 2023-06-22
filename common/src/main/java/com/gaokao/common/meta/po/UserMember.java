package com.gaokao.common.meta.po;

import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

/**
 * 用户信息
 *
 * Created on 2021/7/30.
 *bigint long tinyint Boolean
 * @author wyc
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "tb_user_member")
public class UserMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /**
     * 手机号码
     */
    private String phone;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 是否vip
     */
    @Column(name = "is_vip")
    private boolean vipIsOrNot;

    /**
     * vip 到期时间
     */
    @Column(name = "vip_expiration_time")
    private Long vipExpirationTime;

    /**
     * 状态: 0为正常，1为锁定
     */
    private Integer status;

    /**
     * 分数
     */
    private Integer score;

    /**
     * 全省排名
     */
    private Long provinceRank;
    /**
     * 选考科目
     */
    private String subject;
    /**
     * 微信登录openId
     */
    @Column(name = "wx_open_id")
    private String wxOpenId;

    /**
     * 微信登录sKey，用于生成认证token
     */
    @Column(name = "wx_s_key")
    private String wxSKey;

    /**
     * 微信登录sessionKey
     */
    @Column(name = "wx_session_key")
    private String wxSessionKey;

    /**
     * 微信登录头像地址
     */
    @Column(name = "wx_avatar_url")
    private String wxAvatarUrl;

    /**
     * 微信登录时间
     */
    private Long lastVisitTime;

    /**
     * 注册时间
     */
    private Long createTime;

    /**
     * 更新时间
     */
    private Long updateTime;


}
