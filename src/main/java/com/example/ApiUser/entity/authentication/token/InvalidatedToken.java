package com.example.ApiUser.entity.authentication.token;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class InvalidatedToken {
    @Id
    String id;
    Date expiryTime;
}