package org.zooway.antdvpro.exception;

/**
 * 指示出现了统一资源呗并发操作的异常
 *
 * @author zooway
 * @create 2023/12/18
 */
public class ResourceConcurrentOperateException extends BusinessException {

    public ResourceConcurrentOperateException() {
        super("The resource is being manipulated by another caller!", 400);
    }

    public ResourceConcurrentOperateException(String message) {
        super(message, 400);
    }

    public ResourceConcurrentOperateException(String message, Throwable cause) {
        super(message, cause, 400);
    }

    public ResourceConcurrentOperateException(Throwable cause) {
        super("The resource is being manipulated by another caller!", cause, 400);
    }
}
