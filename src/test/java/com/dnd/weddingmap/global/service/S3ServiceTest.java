package com.dnd.weddingmap.global.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.amazonaws.services.s3.AmazonS3;
import java.io.IOException;
import java.net.URL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

  private static final String BUCKET_URL = "https://s3.ap-northeast-2.amazonaws.com/test-bucket";
  private static final String DIRECTORY_NAME = "profile";
  private static final String FILE_NAME = "test.png";

  @InjectMocks
  private S3Service s3Service;

  @Mock
  private AmazonS3 amazonS3;


  @Test
  void upload() throws IOException {
    // given
    String profileUrl = BUCKET_URL + "/" + DIRECTORY_NAME + "/" + FILE_NAME;
    given(amazonS3.getUrl(any(), anyString())).willReturn(new URL(profileUrl));

    MultipartFile multipartFile = new MockMultipartFile(
        "image", FILE_NAME, "image/png", "test".getBytes());

    // when
    String url = s3Service.upload(multipartFile, DIRECTORY_NAME);

    // then
    assertThat(url).isEqualTo(profileUrl);
  }
}