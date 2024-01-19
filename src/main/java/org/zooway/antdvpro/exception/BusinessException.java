package org.zooway.antdvpro.exception;

import lombok.Getter;

/**
 * 通用的业务异常基类
 *
 * @author zooway
 * @create 2023/12/2
 */
@Getter
public class BusinessException extends RuntimeException {
    private final int httpCode;

    public BusinessException() {
        super("Business exception!");
        this.httpCode = 400;
    }

    public BusinessException(String message, int httpCode) {
        super(message);
        this.httpCode = httpCode;
    }

    public BusinessException(String message, Throwable cause, int httpCode) {
        super(message, cause);
        this.httpCode = httpCode;
    }

    public BusinessException(Throwable cause, int httpCode) {
        super("Business exception!", cause);
        this.httpCode = httpCode;
    }
}
