package com.dnd.wedding.domain.jwt.repository;

import com.dnd.wedding.domain.jwt.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRedisRepository extends CrudRepository<RefreshToken, String> {

}
