package com.xqxy.dr.modular.event.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import lombok.Data;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Data
public class DeleteRuleParam extends BaseParam {

    private Long planId;

    /**
     * 事件id
     */
    private String eventId;

    /**
     * 用户名称
     */
    private String consName;

    /**
     * 用户id
     */
    private String consId;

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 标记状态
     */
    private String delRule;

    private String baselineEmpty;

    private String lessThanTen;

    private String lessThanReplycap;

    private String regionRule;

    private String deleted;

    private List<String> delRuleList;

    private List<String> provinceList;
    private List<String> cityList;
    private List<String> countyList;
    private List<String> regulateRangeList;


}
