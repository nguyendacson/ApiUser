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

import java.io.IOException;
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
            throw new AppException(ErrorCode.USER_EXISTED);

        if (userRepository.existsByEmail(userCreationRequest.getEmail()))
            throw new AppException(ErrorCode.EMAIL_EXISTED);

        User user = userMapper.toUser(userCreationRequest);
        var roles = roleRepository.findAllById(List.of("USER"));
        user.setRoles(new HashSet<>(roles));
        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword()));
        user.setEmailVerified(false);
        user.setAvatar("https://res.cloudinary.com/dvkfc1zut/image/upload/v1760547162/avatars/k5kfzphu2wjlhfbbmjte.jpg");

        user = userRepository.save(user);

        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();
        tokenRepository.save(verificationToken);

        try {
            sendEmailToken.sendVerificationEmail(user.getEmail(), token);
        } catch (MessagingException e) {
            // rollback transaction
            throw new AppException(ErrorCode.EMAIL_NOT_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    public UserResponse getMyInfo(String userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasAuthority('CREATE_PERMISSION1')")
    public List<UserResponse> getAllUser() {
        return userMapper.toListUserResponse(userRepository.findAll());
    }

    //    @PostAuthorize("returnObject.username == authentication.username")
    @PreAuthorize("hasRole('ADMIN') or #username  == authentication.name")
    public UserResponse getUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        return userMapper.toUserResponse(user);
    }

    public UserResponse updateUser(String id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOTFOUND));

        userMapper.updateUser(user, userUpdateRequest);
        user.setPassword(passwordEncoder.encode(userUpdateRequest.getPassword()));

        if (userUpdateRequest.getRoles() != null) {
            var roles = roleRepository.findAllById(
                    userUpdateRequest.getRoles().stream().filter(Objects::nonNull).toList()
            );
            user.setRoles(new HashSet<>(roles));
        }

        String token = UUID.randomUUID().toString();
        EmailVerificationToken verificationToken = EmailVerificationToken.builder()
                .token(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusHours(24))
                .build();
        tokenRepository.save(verificationToken);

        try {
            sendEmailToken.sendVerificationEmail(user.getEmail(), token);
        } catch (MessagingException e) {
            // rollback transaction
            throw new AppException(ErrorCode.EMAIL_NOT_EXISTED);
        }

        return userMapper.toUserResponse(userRepository.save(user));
    }

    public String updateAvatar(String userId, Map<String, String> uploadInfo) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        String imageUrl = uploadInfo.get("secure_url");
        String publicId = uploadInfo.get("public_id");

        if (imageUrl == null || publicId == null) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
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
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_NOT_EXISTED));
        sendEmailToken.sendPasswordResetEmail(user);
    }

    public void resetPassword(String token, String newPassword){
        EmailVerificationToken emailVerificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (emailVerificationToken.getExpiryDate().isBefore(LocalDateTime.now()))
            throw new AppException(ErrorCode.TOKEN_EXPIRED);

        User user = emailVerificationToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        tokenRepository.delete(emailVerificationToken);
    }

    public void changePassword(String userId, UserChangePassword userChangePassword){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (!passwordEncoder.matches(userChangePassword.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_INVALID);
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
