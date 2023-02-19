package com.dnd.weddingmap.domain.checklist.checklistsubitem.repository;

import com.dnd.weddingmap.domain.checklist.checklistsubitem.ChecklistSubItem;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistSubItemRepository extends JpaRepository<ChecklistSubItem, Long> {

  List<ChecklistSubItem> findAllByChecklistItemId(Long id);
}
