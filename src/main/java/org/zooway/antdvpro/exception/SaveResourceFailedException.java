package org.zooway.antdvpro.exception;

/**
 * 指示保存资源时出现异常, 持久化目标可能是db, file等
 *
 * @author zooway
 * @create 2024/1/11
 */
public class SaveResourceFailedException extends BusinessException {
    public SaveResourceFailedException() {
        super("Save resource failed!", 500);
    }

    public SaveResourceFailedException(String message) {
        super(message, 500);
    }

    public SaveResourceFailedException(String message, Throwable cause) {
        super(message, cause, 500);
    }

    public SaveResourceFailedException(Throwable cause) {
        super("Save resource failed!", cause, 500);
    }
}
