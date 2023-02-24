package com.dnd.weddingmap.domain.wedding.repository;

import com.dnd.weddingmap.domain.wedding.Wedding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeddingRepository extends JpaRepository<Wedding, Long> {

}
