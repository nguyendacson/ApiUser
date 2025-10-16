package com.example.ApiUser.service.authentication.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.example.ApiUser.entity.authentication.users.CloudinaryCleanup;
import com.example.ApiUser.repository.authentication.users.CloudinaryCleanupRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("unchecked")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryCleanupService {
    Cloudinary cloudinary;
    CloudinaryCleanupRepository cloudinaryCleanupRepository;

    public Map<String, String> upload(MultipartFile file) throws IOException {
        Map<String, Object> uploadResult = cloudinary.uploader().upload(
                file.getBytes(),
                ObjectUtils.asMap(
                        "folder", "avatars",
                        "transformation", new Transformation<>()
                                .width(1000)
                                .height(1000)
                                .crop("fill")
                                .gravity("auto")
                )
        );

        return Map.of(
                "secure_url", uploadResult.get("secure_url").toString(),
                "public_id", uploadResult.get("public_id").toString()
        );
    }

    public void markForDeletion(String publicId) {
        CloudinaryCleanup record = CloudinaryCleanup.builder()
                .publicId(publicId)
                .build();
        cloudinaryCleanupRepository.save(record);
    }

    public void cleanupOldImages() {
        List<CloudinaryCleanup> list = cloudinaryCleanupRepository.findAllByDeletedFalse();
        for (CloudinaryCleanup item : list) {
            try {
                cloudinary.uploader().destroy(item.getPublicId(), ObjectUtils.emptyMap());
                item.setDeleted(true);
                cloudinaryCleanupRepository.save(item);
            } catch (Exception e) {
                log.info("ERROR CLEAN AVATAR AGAIN", e);
            }
        }
    }


}


