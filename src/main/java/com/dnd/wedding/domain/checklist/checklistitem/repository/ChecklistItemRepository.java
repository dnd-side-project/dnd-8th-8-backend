package com.dnd.wedding.domain.checklist.checklistitem.repository;

import com.dnd.wedding.domain.checklist.checklistitem.ChecklistItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistItemRepository extends JpaRepository<ChecklistItem, Long> {

  List<ChecklistItem> findByMemberId(Long id);
}