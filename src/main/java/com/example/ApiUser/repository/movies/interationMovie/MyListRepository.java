package com.example.ApiUser.repository.movies.interationMovie;

import com.example.ApiUser.entity.authentication.users.User;
import com.example.ApiUser.entity.callMovies.Movie;
import com.example.ApiUser.entity.movies.MyList;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MyListRepository extends JpaRepository<MyList, String> {
    boolean existsByUserAndMovie(User user, Movie movie);

    Optional<MyList> findByUserAndMovie(User user, Movie movie);

    List<MyList> findAllByUser(User user, Sort sort);
}
