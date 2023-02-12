package com.dnd.wedding.domain.checklist.checklistsubitem.repository;

import com.dnd.wedding.domain.checklist.checklistsubitem.ChecklistSubItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChecklistSubItemRepository extends JpaRepository<ChecklistSubItem, Long> {

  List<ChecklistSubItem> findAllByChecklistItem_Id(Long id);

  Optional<ChecklistSubItem> findOneById(Long id);
}
