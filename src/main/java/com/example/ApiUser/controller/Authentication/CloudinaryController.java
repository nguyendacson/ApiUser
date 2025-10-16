package com.example.ApiUser.controller.Authentication;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/cloudinary")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryController {
    Cloudinary cloudinary;

    @GetMapping("/signature")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getUploadSignature() {
        long timestamp = System.currentTimeMillis() / 1000;
        Map paramsToSign = ObjectUtils.asMap(
                "timestamp", timestamp,
                "folder", "avatars"
        );

        String signature = cloudinary.apiSignRequest(paramsToSign,
                cloudinary.config.apiSecret);

        Map<String, Object> result = Map.of(
                "signature", signature,
                "timestamp", timestamp,
                "apiKey", cloudinary.config.apiKey,
                "cloudName", cloudinary.config.cloudName,
                "folder", "avatars"
        );

        return ResponseEntity.ok(result);
    }
}
