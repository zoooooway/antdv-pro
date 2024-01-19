package org.zooway.antdvpro.auth.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.zooway.antdvpro.model.base.Resp;
import org.zooway.antdvpro.util.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 处理认证失败
 *
 * @author zooway
 * @create 2024/1/2
 */
@Slf4j
@Component
public class FailureAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        int code = HttpStatus.UNAUTHORIZED.value();
        String msg = String.format("请求访问：%s，认证失败，无法访问系统资源", request.getRequestURI());

        renderString(response, JsonUtil.writeJson(Resp.error(code, msg)));
    }

    private void renderString(HttpServletResponse response, String string) {
        try {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            log.error("写入响应时异常", e);
        }
    }
}
