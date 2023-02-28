package com.dnd.weddingmap.domain.contract;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.dnd.weddingmap.domain.contract.dto.ContractDto;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ContractTest {

  private final Contract contract = Contract.builder()
      .title("title")
      .contents("contents")
      .contractDate(LocalDate.parse("2022-01-01"))
      .contractStatus(ContractStatus.PROVISIONAL)
      .file("file_url")
      .memo("memo")
      .build();

  @Test
  @DisplayName("계약서 아이디 조회")
  void getId() {
    assertNull(contract.getId());
  }

  @Test
  @DisplayName("계약서 제목 조회")
  void getTitle() {
    assertEquals("title", contract.getTitle());
  }

  @Test
  @DisplayName("계약서 내용 조회")
  void getContents() {
    assertEquals("contents", contract.getContents());
  }

  @Test
  @DisplayName("계약 날짜 조회")
  void getContractDate() {
    assertEquals(LocalDate.parse("2022-01-01"), contract.getContractDate());
  }

  @Test
  @DisplayName("계약 상태 조회")
  void getContractStatus() {
    assertEquals(ContractStatus.PROVISIONAL, contract.getContractStatus());
  }

  @Test
  @DisplayName("계약서 파일 조회")
  void getFile() {
    assertEquals("file_url", contract.getFile());
  }

  @Test
  @DisplayName("계약서 메모 조회")
  void getMemo() {
    assertEquals("memo", contract.getMemo());
  }

  @Test
  @DisplayName("BaseTimeEntity 생성 시간 조회")
  void getCreatedAt() {
    assertNull(contract.getCreatedAt());
  }

  @Test
  @DisplayName("BaseTimeEntity 수정 시간 조회")
  void getModifiedAt() {
    assertNull(contract.getModifiedAt());
  }

  @Test
  @DisplayName("계약서 내용 수정")
  void update() {
    String title = "test title";
    String contents = "test contents";
    LocalDate contractDate = LocalDate.now();
    ContractStatus contractStatus = ContractStatus.IN_PROGRESS;
    String memo = "test memo";

    contract.update(ContractDto.builder()
        .title(title)
        .contents(contents)
        .contractDate(contractDate)
        .contractStatus(contractStatus)
        .memo(memo)
        .build());

    assertEquals(title, contract.getTitle());
    assertEquals(contents, contract.getContents());
    assertEquals(contractDate, contract.getContractDate());
    assertEquals(contractStatus, contract.getContractStatus());
    assertEquals(memo, contract.getMemo());
  }

  @Test
  @DisplayName("계약서 파일 수정")
  void updateFile() {
    String file = "test_file_url";
    contract.updateFile(file);
    assertEquals(file, contract.getFile());
  }
}
