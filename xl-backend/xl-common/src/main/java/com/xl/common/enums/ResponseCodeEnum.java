package com.xl.common.enums;

//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseCodeEnum {

    // Operation successful
    SUCCESS(200,"success"),
    // Login、Session段1~50
    NEED_LOGIN(50,"Login again please"),
    LOGIN_FAILED(51,"Incorrect username or password"),
    TOKEN_INVALID(52,"Invalid session"),
    TOKEN_EXPIRE(53,"Session has expired"),
    TOKEN_REQUIRE(54,"Token is mandatory"),
    // Sign验签 100~120
    SIGN_INVALID(100,"Invalid sign"),
    SIG_TIMEOUT(101,"Sign has expired"),
    // HTTP状态码异常枚举
    BAD_REQUEST(400, "Bad request"),
    UN_AUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not found"),
    SERVER_ERROR(500, "Internal server error"),
    // 参数错误 500~1000
    PARAM_REQUIRE(500,"Missing mandatory parameters"),
    PARAM_INVALID(501,"Invalid parameters"),
    PARAM_FORMAT_ERROR(502,"Format error"),
    // 数据错误 1000~2000
    DATA_EXIST(1000,"Data already exists"),
    NO_RESULT_FOUND(1001,"No result found for the query"),
    DATA_NOT_EXIST(1002,"Data does not exist"),
    QUERY_EXECUTION_ERROR(1003,"Error executing the query"),
    DATA_INTEGRITY_VIOLATION(1004,"Data integrity violation"),
    // 数据错误 3000~3500
    NO_PERMISSION(3000,"No permission");
    int code;
    String message;

    ResponseCodeEnum(int code, String message){
        this.code = code;
        this.message = message;
    }
//    @JSONField(value = true)
    public int getCode() {
        return code;
    }

    public String getmessage() {
        return message;
    }
}
