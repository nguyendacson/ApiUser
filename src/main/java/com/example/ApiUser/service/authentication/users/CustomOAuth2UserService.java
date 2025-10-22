package com.example.ApiUser.service.authentication.users;

import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.repository.authentication.RoleRepository;
import com.example.ApiUser.repository.authentication.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    UserRepository userRepository;
    RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User googleUser = new DefaultOAuth2UserService().loadUser(userRequest);

        String email = googleUser.getAttribute("email");

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            var roles = new HashSet<>(roleRepository.findAllById(Set.of("USER")));

            user = User.builder()
                    .roles(roles)
                    .name(googleUser.getName())
                    .username(email)
                    .email(email)
                    .provider("Google")
                    .emailVerified(true)
                    .avatar(googleUser.getAttribute("picture"))
                    .build();
            userRepository.save(user);
        } else {
            return googleUser;
        }

        return googleUser;
    }
}