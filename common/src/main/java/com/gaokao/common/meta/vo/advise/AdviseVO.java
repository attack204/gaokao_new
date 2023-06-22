package com.gaokao.common.meta.vo.advise;

import com.gaokao.common.meta.vo.volunteer.VolunteerVO;
import lombok.Data;

/**
 * @author MaeYon-Z
 * date 2021-08-10
 */
@Data
public class AdviseVO {
    /*
    * 录取几率
    * */
    private int rate;

    /*
    * 录取概率描述，可冲击、较稳妥、可保底
    * */
    private String rateDesc;


    private VolunteerVO volunteerVO;
}
