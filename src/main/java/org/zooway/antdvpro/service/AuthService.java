package org.zooway.antdvpro.service;

import org.zooway.antdvpro.auth.entity.LoginUser;
import org.zooway.antdvpro.model.dto.AuthMenuDTO;

import java.util.List;

/**
 * @author zooway
 * @create 2024/1/2
 */
public interface AuthService {
    String login(String username, String password);

    void logout(LoginUser loginUser);

    List<AuthMenuDTO> getUserMenu(LoginUser loginUser);
}
