package com.dnd.wedding.domain.aws.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class S3Service {

  @Value("${aws.s3.bucket}")
  private String bucket;

  private final AmazonS3 amazonS3;

  public String upload(MultipartFile multipartFile) throws IOException {
    String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

    ObjectMetadata objMeta = new ObjectMetadata();
    objMeta.setContentLength(multipartFile.getInputStream().available());

    amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

    return amazonS3.getUrl(bucket, s3FileName).toString();
  }
}

