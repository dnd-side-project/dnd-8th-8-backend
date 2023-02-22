package com.dnd.weddingmap.domain.jwt.repository;

import com.dnd.weddingmap.domain.jwt.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, Long> {

}
