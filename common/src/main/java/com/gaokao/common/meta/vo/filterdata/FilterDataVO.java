package com.gaokao.common.meta.vo.filterdata;

import lombok.Data;

import java.util.List;

/**
 * @author MaeYon-Z
 * date 2021-08-22
 */
@Data
public class FilterDataVO {

    private Integer value;

    private String label;

    private List<FilterDataParams> children;

}
