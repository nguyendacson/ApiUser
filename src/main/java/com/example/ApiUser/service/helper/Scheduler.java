package com.example.ApiUser.service.helper;


import com.example.ApiUser.service.authentication.cloudinary.CloudinaryCleanupService;
import com.example.ApiUser.service.authentication.users.UserService;
import com.example.ApiUser.service.movies.interactionService.watching.WatchingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Scheduler {
    WatchingService watchingService;
    CloudinaryCleanupService cloudinaryCleanupService;
    UserService userService;

    //    @PostConstruct   // chạy ngay lập tức
//    @Scheduled(cron = "0 30 23 * * *")
//    @Scheduled(cron = "* * * * * MON")
//    @Scheduled(cron = "0 30 23 * * MON")
    @Scheduled(cron = "0 0 0 1 1,2 *")
    public void cleanupdWatchingAuto() {
        log.info("Start clean data Watching last ...");
        watchingService.deleteOldWatchingList();
    }


    @Scheduled(cron = "0 0 0 1 1,2 *")
    public void cleanupAvatarAuto() {
        log.info("Start clean data Avatar last ...");
        cloudinaryCleanupService.cleanupOldImages();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void cleanupTokenEmailAuto() {
        log.info("Start clean data Token Email ...");
        userService.deleteOldWatchingList();
    }
}
