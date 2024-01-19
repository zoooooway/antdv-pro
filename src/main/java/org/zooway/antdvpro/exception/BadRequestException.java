package org.zooway.antdvpro.exception;

/**
 * 指示请求错误的异常, 通常是由参数错误所引发
 *
 * @author zooway
 * @create 2023/12/26
 */
public class BadRequestException extends BusinessException {

    public BadRequestException() {
        super("Bad request!", 400);
    }

    public BadRequestException(String message) {
        super(message, 400);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause, 400);
    }

    public BadRequestException(Throwable cause) {
        super("Bad request!", cause, 400);
    }
}
