package com.dnd.weddingmap.domain.contract.repository;

import com.dnd.weddingmap.domain.contract.Contract;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

  List<Contract> findByMemberIdOrderByContractDate(Long id);
}
