package com.xl.common.enums;

//@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ResponseCodeEnum {

    // Operation successful
    SUCCESS(200,"Success"),
    // Login、Session段1~50
    NEED_LOGIN(50,"Login Again Please"),
    LOGIN_FAILED(51,"Incorrect username or password"),
    TOKEN_INVALID(52,"Invalid Session"),
    TOKEN_EXPIRE(53,"Session Has Expired"),
    TOKEN_REQUIRE(54,"Token Is Mandatory"),
    // Sign验签 100~120
    SIGN_INVALID(100,"Invalid Sign"),
    SIG_TIMEOUT(101,"Sign Has Expired"),
    // HTTP状态码异常枚举
    BAD_REQUEST(400, "Bad Request"),
    UN_AUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    SERVER_ERROR(500, "Internal Server Error"),
    // 参数错误 500~1000
    PARAM_REQUIRE(500,"Missing Mandatory Parameters"),
    PARAM_INVALID(501,"Invalid Parameters"),
    PARAM_FORMAT_ERROR(502,"Format Error"),
    // 数据错误 1000~2000
    DATA_EXIST(1000,"Data Already Exists"),
    NO_RESULT_FOUND(1001,"No result found for the query"),
    DATA_NOT_EXIST(1002,"Data Does Not Exist"),
    QUERY_EXECUTION_ERROR(1003,"Error executing the query"),
    DATA_INTEGRITY_VIOLATION(1004,"Data integrity violation"),
    // 数据错误 3000~3500
    NO_PERMISSION(3000,"No Permission");
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
