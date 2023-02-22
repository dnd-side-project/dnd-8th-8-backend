package com.dnd.weddingmap.global.service;

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

  public String upload(MultipartFile multipartFile, String directory) throws IOException {
    String fileName = multipartFile.getOriginalFilename();
    String uuid = UUID.randomUUID().toString();
    String saveFileName = directory + "/" + uuid + "_" + fileName;

    ObjectMetadata objectMetadata = new ObjectMetadata();
    objectMetadata.setContentType(multipartFile.getContentType());
    objectMetadata.setContentLength(multipartFile.getSize());

    amazonS3.putObject(bucket, saveFileName, multipartFile.getInputStream(), objectMetadata);

    return amazonS3.getUrl(bucket, saveFileName).toString();
  }
}

