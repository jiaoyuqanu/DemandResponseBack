package com.xqxy.dr.modular.grsg.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xqxy.core.exception.ErrorType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.time.Instant;
import java.time.ZonedDateTime;

@Data
public class GrsgResult {


    @ApiModelProperty(value = "处理结果Code", required = true)
    private String errorCode;
    @ApiModelProperty(value = "处理结果描述信息")
    private String errorMsg;
    @ApiModelProperty(value = "处理结果数据信息")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object data;

    /**
     * @param errorType
     */
    public GrsgResult(ErrorType errorType) {
        this.errorCode = errorType.getCode();
        this.errorMsg = errorType.getMesg();
    }

    /**
     * @param errorType
     * @param data
     */
    public GrsgResult(ErrorType errorType, T data) {
        this(errorType);
        this.data = data;
    }

    /**
     * 内部使用，用于构造成功的结果
     *
     * @param errorCode
     * @param mesg
     * @param data
     */
    private GrsgResult(String errorCode, String mesg, Object data) {
        this.errorCode = errorCode;
        this.errorMsg = mesg;
        this.data = data;
    }
    /**
     * 快速创建成功结果并返回结果数据
     *
     * @param data
     * @return Result
     */
    public static GrsgResult success(Object data) {
        return new GrsgResult("200", "申请成功", data);
    }
    public static GrsgResult success() {
        return new GrsgResult("200", "申请成功", "");
    }

    public static GrsgResult success(String mesg,Object data) {
        return new GrsgResult("200", mesg, data);
    }

    public static GrsgResult error() {
        return new GrsgResult("500", "申请失败", "");
    }
    public static GrsgResult error(Object data) {
        return new GrsgResult("000", "申请失败", data);
    }

    public static GrsgResult error(String mesg,Object data) {
        return new GrsgResult("000", "申请失败", data);
    }
}
