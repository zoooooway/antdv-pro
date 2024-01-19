package org.zooway.antdvpro.exception;

/**
 * 用于无法找到资源异常
 *
 * @author zooway
 * @create 2023/11/24
 */
public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException() {
        super("Unable to find resource!", 404);
    }

    public ResourceNotFoundException(String message) {
        super(message, 404);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause, 404);
    }

    public ResourceNotFoundException(Throwable cause) {
        super("Unable to find resource!", cause, 404);
    }
}
