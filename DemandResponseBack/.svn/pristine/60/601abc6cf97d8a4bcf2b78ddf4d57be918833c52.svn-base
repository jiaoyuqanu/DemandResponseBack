package com.xqxy.sys.modular.cust.param;

import com.xqxy.core.pojo.base.param.BaseParam;
import com.xqxy.dr.modular.project.entity.CustContractFile;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class BusConfigParam extends BaseParam {

    private String busId;
    /**
     * 业务接口。申请对应按钮的操作方法接口，这样可以作为一个触发条件，来找到审批人
     * */
    private String busApi;
    /**
     *申请人组织机构id
     * */
    private String applyOrgId;
    /**
     * 业务类型
     * */
    private Integer busType;
    /**
     * 申请人id
     * */
    private Long applyManId;

    /**
     * 申请人name，非必传
     */
    private String applyManName;

    /**
     * 审核意见，申请人提交申请单的时候，调用接口，值可为空，当审核人审核申请单的时候，必须填写审核意见，值必须不为空！
     *
     * */
    private String checkMess;

    /**
     * 审核结果，申请人提交申请单生成待办事项的时候，审核结果值可为空，当审核人审核申请单的时候，值必须不为空。
     *                     3：同意；4：驳回
     * */
    private Integer checkResult;

    /**
     *          申请描述
     * */
    private String descr;

    /**
     * 必传，审核层级，申请传1。审核时候该值是前端从代办数据里取
     */
    private Integer level;

    /**
     * 必传，操作人Id，申请时是申请人，审核是是审核人，后端从当前登录用户获取
     */
    private String operaManId;

    /**
     * 必传，操作人Id，申请时是申请人，审核是是审核人，后端从当前登录用户获取
     */
    private String integrator;

    /**
     *  上传文件  协议 和 承诺书
     */
    private List<CustContractFile> custContractFiles;

    private Long fileId;

    private String fileName;
}
