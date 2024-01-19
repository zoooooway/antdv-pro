package org.zooway.antdvpro.exception;

/**
 * 用于指示外部服务调用异常
 *
 * @author zooway
 * @create 2023/11/24
 */
public class OutsideAPIInvokeException extends RuntimeException {
    public OutsideAPIInvokeException() {
        super("Outside api invoke exception!");
    }

    public OutsideAPIInvokeException(String message) {
        super(message);
    }

    public OutsideAPIInvokeException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutsideAPIInvokeException(Throwable cause) {
        super("Outside api invoke exception!", cause);
    }
}
