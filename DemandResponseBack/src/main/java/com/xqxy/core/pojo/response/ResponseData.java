
package com.xqxy.core.pojo.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.xqxy.core.exception.BaseException;
import com.xqxy.core.exception.ErrorType;
import com.xqxy.core.exception.SystemErrorType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * 响应结果数据
 *
 * @author xiao jun
 * @date 2020/3/30 15:04
 */
@ApiModel(description = "rest请求的返回模型，所有rest正常都返回该类的对象")
@Data
@Setter
public class ResponseData<T> {

    public static final String SUCCESSFUL_CODE = "000000";
    public static final String SUCCESSFUL_MESG = "处理成功";

    @ApiModelProperty(value = "处理结果code", required = true)
    private String code;
    @ApiModelProperty(value = "处理结果描述信息")
    private String mesg;
    @ApiModelProperty(value = "请求结果生成时间戳")
    private Instant time;
    @ApiModelProperty(value = "处理结果数据信息")
//    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public ResponseData() {
        this.time = ZonedDateTime.now().toInstant();
    }

    /**
     * @param errorType
     */
    public ResponseData(ErrorType errorType) {
        this.code = errorType.getCode();
        this.mesg = errorType.getMesg();
        this.time = ZonedDateTime.now().toInstant();
    }

    /**
     * @param errorType
     * @param data
     */
    public ResponseData(ErrorType errorType, T data) {
        this(errorType);
        this.data = data;
    }

    /**
     * 内部使用，用于构造成功的结果
     *
     * @param code
     * @param mesg
     * @param data
     */
    private ResponseData(String code, String mesg, T data) {
        this.code = code;
        this.mesg = mesg;
        this.data = data;
        this.time = ZonedDateTime.now().toInstant();
    }

    /**
     * 快速创建成功结果并返回结果数据
     *
     * @param data
     * @return Result
     */
    public static ResponseData success(Object data) {
        return new ResponseData<>(SUCCESSFUL_CODE, SUCCESSFUL_MESG, data);
    }

    public static ResponseData success(String mesg,Object data) {
        return new ResponseData<>(SUCCESSFUL_CODE, mesg, data);
    }

    /**
     * 快速创建成功结果
     *
     * @return Result
     */
    public static ResponseData success() {
        return success(null);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @return Result
     */
    public static ResponseData fail() {
        return new ResponseData(SystemErrorType.SYSTEM_ERROR);
    }

    /**
     * 系统异常类没有返回数据
     *
     * @param baseException
     * @return Result
     */
    public static ResponseData fail(BaseException baseException) {
        return fail(baseException, null);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param data
     * @return Result
     */
    public static ResponseData fail(BaseException baseException, Object data) {
        return new ResponseData<>(baseException.getErrorType(), data);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType
     * @param data
     * @return Result
     */
    public static ResponseData fail(ErrorType errorType, Object data) {
        return new ResponseData<>(errorType, data);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param errorType
     * @return Result
     */
    public static ResponseData fail(ErrorType errorType) {
        return ResponseData.fail(errorType, null);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param data
     * @return Result
     */
    public static ResponseData fail(String code,String mesg,Object data) {
        return new ResponseData<>(code,mesg,data);
    }

    /**
     * 系统异常类并返回结果数据
     *
     * @param data
     * @return Result
     */
    public static ResponseData fail(Object data) {
        return new ResponseData<>(SystemErrorType.SYSTEM_ERROR, data);
    }


    /**
     * 成功code=000000
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isSuccess() {
        return SUCCESSFUL_CODE.equals(this.code);
    }

    /**
     * 失败
     *
     * @return true/false
     */
    @JsonIgnore
    public boolean isFail() {
        return !isSuccess();
    }
}
