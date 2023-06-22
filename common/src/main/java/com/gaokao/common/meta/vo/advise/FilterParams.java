package com.gaokao.common.meta.vo.advise;

import lombok.Data;

import java.util.List;

/**
 * @author MaeYon-Z
 * date 2021-08-28
 */
@Data
public class FilterParams {

    private Long userId;

    private Integer score;

    private Integer page;

    private Integer limit;

    private Integer total;

    //批次
    private List<Integer> batch;
    //地区
    private List<Integer> region;
    //大学类型
    private List<Integer> schoolType;
    //大学特色
    private List<Integer> schoolTeSe;
    //大学性质
    private List<Integer> schoolXingZhi;

    /*
    * 暂时没有专业类型数据
    * private List<Integer> majorType;
    * */


    private String universityName;

    private String majorName;

    private Integer type;
}
