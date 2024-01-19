package org.zooway.antdvpro.auth.provider;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zooway.antdvpro.auth.entity.LoginUser;
import org.zooway.antdvpro.util.JsonUtil;
import org.zooway.antdvpro.util.RedisUtil;

import javax.annotation.Resource;
import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.zooway.antdvpro.common.constant.RequestConstants.NULL;
import static org.zooway.antdvpro.common.constant.RequestConstants.UNDEFINED;

/**
 * 提供jwt token的相关服务
 *
 * @author zooway
 * @create 2024/1/2
 */
@Slf4j
@Component
public class TokenService implements InitializingBean {
    public static final String LOGIN_USER_KEY = "login_user_key";
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";
    private static final Long REFRESH_DIFF_MS = TimeUnit.MINUTES.toMillis(10);
    /**
     * 令牌自定义标识
     */
    @Value("${antdv-pro.jwt.token-header}")
    private String header;
    /**
     * 令牌秘钥
     */
    @Value("${antdv-pro.jwt.secret}")
    private String secret;
    /**
     * 令牌有效期(分钟)
     */
    @Value("${antdv-pro.jwt.expire-time-minutes}")
    private int expireTimeMinutes;

    @Resource
    private RedisUtil redisUtil;

    private SecretKey secretKey;
    private JwtParser jwtParser;

    public static void main(String[] args) {
        // 生成一个随机密钥
        SecretKey key = Jwts.SIG.HS256.key().build();
        String secretString = Encoders.BASE64.encode(key.getEncoded());
        System.out.println(secretString);
    }

    @Override
    public void afterPropertiesSet() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        jwtParser = Jwts.parser().verifyWith(secretKey).build();

    }

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request) {
        // 获取请求携带的令牌
        String token = getToken(request);
        if (StringUtils.isNotBlank(token) && !UNDEFINED.equalsIgnoreCase(token) && !NULL.equalsIgnoreCase(token)) {
            Claims claims = parseToken(token);
            // 解析对应的权限以及用户信息
            String uuid = (String) claims.get(LOGIN_USER_KEY);
            String userKey = getTokenKey(uuid);
            String userJson = redisUtil.get(userKey);
            if (StringUtils.isNotBlank(userJson)) {
                return JsonUtil.readJson(userJson, LoginUser.class);
            }
        }


        return null;
    }

    /**
     * 设置用户身份信息
     */
    public void setLoginUser(LoginUser loginUser) {
        if (loginUser != null && StringUtils.isNotBlank(loginUser.getUuid())) {
            refreshToken(loginUser);
        }
    }

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String uuid) {
        if (StringUtils.isNotBlank(uuid)) {
            String userKey = getTokenKey(uuid);
            redisUtil.del(userKey);
        }
    }

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser) {
        String uuid = UUID.randomUUID().toString();
        loginUser.setUuid(uuid);
        refreshToken(loginUser);

        Map<String, Object> claims = new HashMap<>();
        claims.put(LOGIN_USER_KEY, uuid);
        return createToken(claims);
    }

    /**
     * 验证令牌有效期，相差不足10分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser) {
        long expireDeadline = loginUser.getExpireTime();
        long currentTime = System.currentTimeMillis();
        if (expireDeadline - currentTime <= REFRESH_DIFF_MS) {
            refreshToken(loginUser);
        }
    }

    /**
     * 刷新令牌有效期
     *
     * @param loginUser 登录信息
     */
    public void refreshToken(LoginUser loginUser) {
        loginUser.setLoginTime(System.currentTimeMillis());
        loginUser.setExpireTime(loginUser.getLoginTime() + TimeUnit.MINUTES.toMillis(expireTimeMinutes));
        // 根据uuid将loginUser缓存
        String userKey = getTokenKey(loginUser.getUuid());
        String userJson = JsonUtil.writeJson(loginUser);
        redisUtil.set(userKey, userJson, expireTimeMinutes, TimeUnit.MINUTES);
    }

    /**
     * 从数据声明生成令牌
     *
     * @param claims 数据声明
     * @return 令牌
     */
    private String createToken(Map<String, Object> claims) {
        String token = Jwts.builder()
                .claims(claims)
                .signWith(secretKey)
                .compact();
        return token;
    }

    /**
     * 从令牌中获取数据声明
     *
     * @param token 令牌
     * @return 数据声明
     */
    private Claims parseToken(String token) {
        return jwtParser.parseSignedClaims(token).getPayload();
    }

    /**
     * 获取请求token
     *
     * @param request
     * @return token
     */
    private String getToken(HttpServletRequest request) {
        return request.getHeader(header);
    }

    public String getTokenKey(String uuid) {
        return LOGIN_TOKEN_KEY + uuid;
    }
}
