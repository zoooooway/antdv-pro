package org.zooway.antdvpro.exception.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;
import org.zooway.antdvpro.exception.BusinessException;
import org.zooway.antdvpro.model.base.Resp;


/**
 * 全局异常处理
 *
 * @author yechao 2016年5月13日 下午4:16:41
 * @version V1.0
 */
@Slf4j
@RestControllerAdvice
public class DefaultExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Resp> methodArgumentNotValidException(NativeWebRequest request, MethodArgumentNotValidException mae) {
        log.error("", mae);
        Resp resp = new Resp(null, new Resp.Meta(400, mae.getMessage()));
        return ResponseEntity.status(200)
                .body(resp);
    }


    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Resp> businessException(BusinessException re) {
        log.error("", re);
        Resp resp = new Resp(null, new Resp.Meta(re.getHttpCode(), re.getMessage()));
        return ResponseEntity.status(200)
                .body(resp);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Resp> rtException(RuntimeException re) {
        log.error("", re);
        Resp resp = new Resp(null, new Resp.Meta(500, re.getMessage()));
        return ResponseEntity.status(500)
                .body(resp);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Resp> exception(NativeWebRequest request, Exception re) {
        log.error("", re);
        Resp resp = new Resp(null, new Resp.Meta(500, re.getMessage()));
        return ResponseEntity.status(500)
                .body(resp);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Resp> authenticationException(AuthenticationException exception) {
        log.error("认证异常.", exception);
        Resp resp = new Resp(null, new Resp.Meta(401, exception.getMessage()));
        return ResponseEntity.status(401)
                .body(resp);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Resp> accessDeniedException(AccessDeniedException exception) {
        log.error("不允许访问.", exception);
        Resp resp = new Resp(null, new Resp.Meta(403, exception.getMessage()));
        return ResponseEntity.status(200)
                .body(resp);
    }
}
