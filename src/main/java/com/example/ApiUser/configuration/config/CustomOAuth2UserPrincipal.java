package com.example.ApiUser.configuration.config;

import com.example.ApiUser.entity.authentication.users.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class CustomOAuth2UserPrincipal implements OidcUser {

    private final User user;
    private final OidcUser oidcUser;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomOAuth2UserPrincipal(User user, OidcUser oidcUser) {
        this.user = user;
        this.oidcUser = oidcUser;

        Set<GrantedAuthority> mergedAuthorities = new HashSet<>(oidcUser.getAuthorities());

        Set<GrantedAuthority> dbAuthorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toSet());

        mergedAuthorities.addAll(dbAuthorities);

        this.authorities = mergedAuthorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getName() {
        return user.getEmail();
    }


    @Override
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }
}