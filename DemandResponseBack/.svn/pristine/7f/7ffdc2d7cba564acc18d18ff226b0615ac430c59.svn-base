package com.xqxy.dr.modular.project.params;

import com.xqxy.core.pojo.base.param.BaseParam;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

@ApiModel(description = "需求响应项目审核 参数")
@EqualsAndHashCode(callSuper = true)
@Data
public class ExamineParam extends BaseParam {

    @NotNull(message = "busId不能为空，请检查busId参数", groups = {add.class})
    private String busId;
    /**
     * 业务接口。申请对应按钮的操作方法接口，这样可以作为一个触发条件，来找到审批人
     * */
    private String busApi;
    /**
     *申请人组织机构id
     * */
    @NotNull(message = "applyOrgId申请人组织机构id不能为空，请检查applyOrgId参数", groups = {add.class})
    private String applyOrgId;
    /**
     * 业务类型
     * */
    @NotNull(message = "busType业务类型不能为空，请检查busType参数", groups = {add.class})
    private Integer busType;
    /**
     * 申请人id
     * */
    @NotNull(message = "applyManId申请人id不能为空，请检查applyManId参数", groups = {add.class})
    private Long applyManId;

    /**
     * 申请人name，非必传
     */
    private String applyManName;


    /**
     * 审核意见，申请人提交申请单的时候，调用接口，值可为空，当审核人审核申请单的时候，必须填写审核意见，值必须不为空！
     * */
    @NotNull(message = "checkMess审核意见不能为空，请检查checkMess参数", groups = {add.class})
    private String checkMess;

    /**
     * 审核结果，申请人提交申请单生成待办事项的时候，审核结果值可为空，当审核人审核申请单的时候，值必须不为空。
     *                     3：同意；4：驳回
     * */
    @NotNull(message = "checkResult审核结果不能为空，请检查checkResult参数", groups = {add.class})
    private Integer checkResult;

    // @NotNull(message = "projectId不能为空，请检查projectId参数", groups = {add.class})
    private Long projectId;

    /**
     *          申请描述
     * */
    private String descr;

    /**
     * 必传，审核层级，申请传1。审核时候该值是前端从代办数据里取
     */
    @NotNull(message = "level审核层级不能为空，请检查level参数", groups = {add.class})
    private Integer level;

    /**
     * 必传，操作人Id，申请时是申请人，审核是是审核人，后端从当前登录用户获取
     */
    private String operaManId;
}
