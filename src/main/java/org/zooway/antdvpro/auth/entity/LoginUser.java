package org.zooway.antdvpro.auth.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.zooway.antdvpro.auth.enums.RoleEnum;
import org.zooway.antdvpro.model.dto.AuthRoleDTO;
import org.zooway.antdvpro.model.po.AuthUserPO;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * @author zooway
 * @create 2024/1/2
 */
@NoArgsConstructor
@Data
@JsonIgnoreProperties(value = {"password", "username", "accountNonExpired", "accountNonLocked", "credentialsNonExpired", "enabled"})
public class LoginUser implements UserDetails {
    private static final long serialVersionUID = 1L;

    private List<AuthRoleDTO> roles;
    private AuthUserPO authUser;

    private String uuid;
    private Long expireTime;
    private Long loginTime;

    private boolean enabled;
    private boolean accountNonExpired;
    private boolean credentialsNonExpired;
    private boolean accountNonLocked;

    public LoginUser(List<AuthRoleDTO> roles) {
        this.roles = roles;
    }

    public boolean checkRoot() {
        return roles.stream().anyMatch(r -> RoleEnum.SUPPER.getKey().equals(r.getRoleKey()));
    }

    /*                  security                               */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return authUser.getPassword();
    }

    @Override
    public String getUsername() {
        return authUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}