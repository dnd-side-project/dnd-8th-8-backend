package com.dnd.weddingmap.domain.contract.repository;

import com.dnd.weddingmap.domain.contract.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

}
