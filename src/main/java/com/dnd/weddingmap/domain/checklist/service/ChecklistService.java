package com.dnd.weddingmap.domain.checklist.service;

import com.dnd.weddingmap.domain.checklist.checklistitem.dto.ChecklistItemApiDto;
import java.util.List;

public interface ChecklistService {

  List<ChecklistItemApiDto> findChecklist(Long memberId);
}
