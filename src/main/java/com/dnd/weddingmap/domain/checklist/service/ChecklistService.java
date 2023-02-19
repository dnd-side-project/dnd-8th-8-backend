package com.dnd.weddingmap.domain.checklist.service;

import com.dnd.weddingmap.domain.checklist.checklistitem.dto.ChecklistItemApiDto;
import com.dnd.weddingmap.domain.checklist.checklistitem.dto.ChecklistItemDto;
import com.dnd.weddingmap.domain.checklist.dto.PreChecklistItemListDto;
import com.dnd.weddingmap.domain.member.Member;
import java.util.List;

public interface ChecklistService {

  List<ChecklistItemApiDto> findChecklist(Long memberId);

  List<ChecklistItemDto> savePreChecklistItemList(
      Member member, PreChecklistItemListDto preChecklistItemListDto);
}
