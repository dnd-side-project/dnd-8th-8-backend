package com.dnd.wedding.domain.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@RedisHash(value = "email")
public class RefreshToken {

  @Id
  private String email;
  private String token;

  @TimeToLive
  private long expiredTime;
}