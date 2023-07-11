package com.xqxy.core.exception;

import lombok.Getter;

@Getter
public enum SystemErrorType implements ErrorType {

    /**
     * 系统内部异常
     */
    SYSTEM_ERROR("-1", "系统异常"),
    SYSTEM_BUSY("000001", "系统繁忙,请稍候再试"),

    GATEWAY_NOT_FOUND_SERVICE("010404", "服务未找到"),
    GATEWAY_ERROR("010500", "网关异常"),
    GATEWAY_CONNECT_TIME_OUT("010002", "网关超时"),

    ARGUMENT_NOT_VALID("020000", "请求参数校验不通过"),
    UPLOAD_FILE_SIZE_LIMIT("020001", "上传文件大小超过限制"),
    UPLOAD_FILE_FORMAT_ERROR("020002", "上传文件格式错误"),
    UPLOAD_FILE_NULL("020003", "上传文件数据为空"),

    DUPLICATE_PRIMARY_KEY("030000", "唯一键冲突"),
    DEL_EXIST_VALID("040000", "存在下级,不能删除"),
    ADD_FAIL("050000", "新增失败"),
    UPDATE_FAIL("060000", "修改失败"),
    DELETE_FAIL("070000", "删除失败"),
    USED_BEFORE_DELETE_FAIL("080000", "已被使用不能删除"),

    /**
     *  业务异常定义
     * 1、系统管理相关业务异常，错误码范围：10000-19999
     * 2、资产相关业务异常，错误码范围：20000-29999
     * 3、资料相关业务异常，错误码范围：30000-39999
     * 4、运维相关  错误码范围：40000-49999
     */
    USER_USERNAME_REPEAT("10000", "用户username重复"),
    USER_EMAIL_NOT_VALID("10001", "邮箱格式校验不通过"),
    USER_ADD_FAIL("10002", "用户新增失败"),
    USER_UPDATE_FAIL("10003", "用户修改失败"),

    ORG_DEL_EXIST_VALID("10004", "组织机构下存在下级机构不能删除"),
    ORG_DEL_USER_VALID("10005", "组织机构下存在关联用户不能删除"),

    PARAMETER_NULL("10006","必传参数为空"),
    AGGREGATOR_NO_REPEAT("10007","负荷聚合商组号重复"),
    DEVICEID_NO_REPEAT("10008","设备编号重复"),
    CLASS_TO_ARRAY("10009","对象转换数组失败"),
    NO_DATA("10009","读取到的文件内容为空"),
    NO_CONTAINS_CONSID("10010","登录用户不代理此户号"),
    ERROR_FILE_TYPE("10011","错误的文件类型"),
    EVENT_NULL("10012","未找到对应事件对象"),
    CONS_RELA_NULL("10013","没有代理任何户号"),

    NULL_ASSET_NO("20001", "未上传资产编号"),
    NULL_UP_ASSET_NO("20003", "上传资产编号文件为空"),
    ERROR_UP_ASSET_NO("20004", "上传资产编号失败"),
    ASSET_NO_SIZE_LIMIT("20002", "资产编号数量不足"),


    START_FAIL("40001", "启动失败"),
    STOP_FAIL("40002", "停止失败"),
    QUERY_FAIL("40003", "获取信息失败"),
    ;


    /**
     * 错误类型码
     */
    private String code;
    /**
     * 错误类型描述信息
     */
    private String mesg;

    SystemErrorType(String code, String mesg) {
        this.code = code;
        this.mesg = mesg;
    }
}
