package com.gaokao.common.meta.vo.advise;

import lombok.Data;

/**
 * @author MaeYon-Z
 * date 2021-08-31
 */
@Data
public class AutoGenerateFormParams extends FilterParams{

    private Integer chongRate;

    private Integer wenRate;

    private Integer baoRate;
}
