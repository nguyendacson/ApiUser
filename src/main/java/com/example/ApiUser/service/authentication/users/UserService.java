package com.example.ApiUser.service.authentication.users;

import com.example.ApiUser.dto.request.authentication.users.UserChangePassword;
import com.example.ApiUser.dto.request.authentication.users.UserCreationRequest;
import com.example.ApiUser.dto.request.authentication.users.UserUpdateRequest;
import com.example.ApiUser.dto.response.authentication.UserResponse;
import com.example.ApiUser.entity.authentication.users.EmailVerificationToken;
import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.exception.AppException;
import com.example.ApiUser.exception.ErrorCode;
import com.example.ApiUser.mapper.authentication.UserMapper;
import com.example.ApiUser.repository.authentication.RoleRepository;
import com.example.ApiUser.repository.authentication.UserRepository;
import com.example.ApiUser.repository.authentication.users.EmailVerificationTokenRepository;
import com.example.ApiUser.service.authentication.cloudinary.CloudinaryCleanupService;
import com.example.ApiUser.service.helper.SendEmailToken;
import jakarta.mail.MessagingException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;
    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    CloudinaryCleanupService cloudinaryCleanupService;
    EmailVerificationTokenRepository tokenRepository;
    SendEmailToken sendEmailToken;

    @Transactional
    public UserResponse createUser(UserCreationRequest userCreationRequest) {
        if (userRepository.existsByUsername(userCreationRequest.getUsername()))
            throw new AppException(ErrorCode.USR_EXISTED);

        String email = userCreationRequest.getEmail();
        if (email != null && !email.isBlank()) {
            if (userRepository.existsByEmail(email))
                throw new AppException(ErrorCode.USR_EMAIL_EXISTED);
        }

        User user = userMapper.toUser(userCreationRequest);
        var roles = roleRepository.findAllById(List.of("USER"));
        user.setRoles(new HashSet<>(roles));
        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        user.setEmailVerified(false);
        user.setAvatar("https://res.cloudinary.com/dvkfc1zut/image/upload/v1760547162/avatars/k5kfzphu2wjlhfbbmjte.jpg");

        user = userRepository.save(user);

        if (user.getEmail() != null) {
            String token = UUID.randomUUID().toString();
            EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                    .token(token)
                    .user(user)
                    .expiryDate(LocalDateTime.now().plusHours(24))
                    .build();
            tokenRepository.save(verificationToken);

            try {
                log.info("TEST");
                sendEmailToken.sendVerificationEmail(user.getEmail(), token);
            } catch (MessagingException e) {
                // rollback transaction
                throw new AppException(ErrorCode.USR_EMAIL_NOT_FOUND);
            }
        }
        return userMapper.toUserResponse(user);
    }

    //    @PostAuthorize("returnObject.username == authentication.username")
    @PreAuthorize("isAuthenticated()")
    public UserResponse getUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        String newEmail = userUpdateRequest.getEmail();
        String oldEmail = user.getEmail();

        boolean shouldSendVerification = false;

        if (StringUtils.hasText(newEmail)) {
            if (!StringUtils.hasText(oldEmail)) {
                if (userRepository.existsByEmail(newEmail)) {
                    throw new AppException(ErrorCode.USR_EMAIL_EXISTED);
                }
                shouldSendVerification = true;
            } else {
                boolean emailChanged = !Objects.equals(oldEmail, newEmail);
                if (emailChanged) {
                    if (userRepository.existsByEmail(newEmail)) {
                        throw new AppException(ErrorCode.USR_EMAIL_EXISTED);
                    }
                    shouldSendVerification = true;
                }
            }
        }

        userMapper.updateUser(user, userUpdateRequest);

        if (shouldSendVerification) {
            String token = UUID.randomUUID().toString();
            EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                    .token(token)
                    .user(user)
                    .expiryDate(LocalDateTime.now().plusHours(24))
                    .build();
            tokenRepository.save(verificationToken);

            try {
                log.info("Gửi mail xác thực cho {}", newEmail);
                sendEmailToken.sendVerificationEmail(newEmail, token);
            } catch (MessagingException e) {
                throw new AppException(ErrorCode.USR_EMAIL_NOT_FOUND);
            }
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }


    public String updateAvatar(String userId, Map<String, String> uploadInfo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        String imageUrl = uploadInfo.get("secure_url");
        String publicId = uploadInfo.get("public_id");

        if (imageUrl == null || publicId == null) {
            throw new AppException(ErrorCode.TKN_INVALID_REQUEST);
        }

        final String KEY_DEFAULT = "k5kfzphu2wjlhfbbmjte";
        String oldPublicId = user.getAvatarPublicId();

        user.setAvatar(imageUrl);
        user.setAvatarPublicId(publicId);
        userRepository.save(user);

        if (oldPublicId != null && !oldPublicId.equals(KEY_DEFAULT)) {
            cloudinaryCleanupService.markForDeletion(oldPublicId);
        }
        return "Updated avatar success";
    }

    public void forgotPassword(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USR_EMAIL_NOT_FOUND));
        sendEmailToken.sendPasswordResetEmail(user);
    }

    public void resetPassword(String token, String newPassword) {
        EmailVerificationToken emailVerificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.TKN_INVALID));

        if (emailVerificationToken.getExpiryDate().isBefore(LocalDateTime.now()))
            throw new AppException(ErrorCode.TKN_EXPIRED);

        User user = emailVerificationToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(emailVerificationToken);
    }

    public void changePassword(String userId, UserChangePassword userChangePassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USR_NOT_FOUND));

        if (!StringUtils.hasText(userChangePassword.getPassword())) {
            throw new AppException(ErrorCode.USR_PASSWORD_REQUIRED);
        }

        if (!StringUtils.hasText(userChangePassword.getNewPassword())) {
            throw new AppException(ErrorCode.USR_NEW_PASSWORD_REQUIRED);
        }

        if (!passwordEncoder.matches(userChangePassword.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.USR_PASSWORD_INVALID);
        }

        user.setPassword(passwordEncoder.encode(userChangePassword.getNewPassword()));
        userRepository.save(user);
    }

    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void deleteOldWatchingList() {
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = tokenRepository.deleteByExpiryDateBefore(now);
        log.info("Đã xóa {} bản ghi token email hết hạn", deletedCount);
    }

}
