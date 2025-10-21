package com.example.ApiUser.repository.movies.interationMovie;

import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.entity.movies.ListForYou;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ListForYouRepository extends JpaRepository<ListForYou, String> {
    List<ListForYou> findAllByUser(User user);

    void deleteAllByUser(User user);
    void deleteAllByUser_Id(String userId);
}