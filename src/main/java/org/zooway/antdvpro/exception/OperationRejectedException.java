package org.zooway.antdvpro.exception;

/**
 * 指示进行的操作被拒绝的异常
 *
 * @author zooway
 * @create 2023/11/24
 */
public class OperationRejectedException extends BusinessException {

    public OperationRejectedException() {
        super("Operation rejected!", 403);
    }

    public OperationRejectedException(String message) {
        super(message, 403);
    }

    public OperationRejectedException(String message, Throwable cause) {
        super(message, cause, 403);
    }

    public OperationRejectedException(Throwable cause) {
        super("Operation rejected!", cause, 403);
    }
}
