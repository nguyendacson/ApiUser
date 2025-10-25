package com.example.ApiUser.service.authentication.users;

import com.example.ApiUser.configuration.config.CustomOAuth2UserPrincipal;
import com.example.ApiUser.entity.authentication.token.Role;
import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.repository.authentication.RoleRepository;
import com.example.ApiUser.repository.authentication.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomOAuth2UserService extends OidcUserService {
    UserRepository userRepository;
    RoleRepository roleRepository;

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("VAO LOAD USER ");
        OidcUser oidcUser = new OidcUserService().loadUser(userRequest);

        log.info("DATA TRA VE EMAIL{}", oidcUser);
        String email = oidcUser.getAttribute("email");

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found from OIDC provider");
        }

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            log.info("Creating new user for email: {}", email);

//            Role userRole = roleRepository.findById("USER")
//                    .orElseThrow(() -> new OAuth2AuthenticationException("Default role with ID 'USER' not found in database."));
            var roles = new HashSet<>(roleRepository.findAllById(Set.of("USER")));

            User newUser = User.builder()
                    .roles(roles)
                    .name(oidcUser.getAttribute("name")) // <-- Sửa ở đây
                    .username(email)
                    .email(email)
                    .provider("Google")
                    .emailVerified(true)
                    .avatar(oidcUser.getAttribute("picture")) // <-- Sửa ở đây
                    .build();

            return userRepository.save(newUser);
        });
        log.info("===> Authenticated user: {}", user.getEmail());
        return new CustomOAuth2UserPrincipal(user, oidcUser);
    }
}