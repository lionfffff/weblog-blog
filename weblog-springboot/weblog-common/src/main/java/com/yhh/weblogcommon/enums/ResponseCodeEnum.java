package com.yhh.weblogcommon.enums;

import com.yhh.weblogcommon.exception.BaseExceptionInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCodeEnum implements BaseExceptionInterface {

    SYSTEM_ERROR("10000", "系统异常"),
    PARAM_NOT_VALID("10001", "参数不合法"),

    PRODUCT_NOT_FOUND("20000", "数据不存在"),
    LOGIN_FAIL("20000", "登录失败"),
    USERNAME_OR_PWD_ERROR("20001", "用户名或密码错误"),
    UNAUTHORIZED("20002", "未登录或登录已失效"),
    USERNAME_NOT_FOUND("20003", "用户名不存在"),
    FORBIDDEN("20004", "无权限访问"),
    CATEGORY_NAME_IS_EXISTED("20005", "分类名称已存在"),
    TAG_CANT_DUPLICATE("20006", "标签不能重复"),
    TAG_NOT_EXIST("20007", "标签不存在"),
    FILE_UPLOAD_FAILED("20008", "文件上传失败"),
    CATEGORY_NOT_EXISTED("20009", "分类不存在"),
    ARTICLE_NOT_FOUND("20010", "文章不存在"),
    CATEGORY_CAN_NOT_DELETE("20011", "该分类下还有文章，无法删除"),
    TAG_CAN_NOT_DELETE("20012", "该标签下还有文章，无法删除"),
    TAG_NOT_EXISTED("20013", "标签不存在"),
    USERNAME_ALREADY_EXISTED("20014", "用户名已存在"),
    BLOG_NOT_FOUND("20015", "博客不存在"),
    BLOG_ACCESS_DENIED("20016", "无权访问该博客内容"),
    USER_NOT_FOUND("20017", "用户不存在");

    private final String errorCode;

    private final String errorMessage;
}
