package com.dnd.weddingmap.domain.budget.service;

import com.dnd.weddingmap.domain.wedding.dto.BudgetDto;

public interface BudgetService {

  BudgetDto getCurrentBudget(Long memberId);
}
