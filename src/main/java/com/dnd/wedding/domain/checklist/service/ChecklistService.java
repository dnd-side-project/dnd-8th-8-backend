package com.dnd.wedding.domain.checklist.service;

import com.dnd.wedding.domain.checklist.checklistitem.dto.ChecklistItemApiDto;
import java.util.List;

public interface ChecklistService {

  List<ChecklistItemApiDto> findChecklist(Long memberId);
}
