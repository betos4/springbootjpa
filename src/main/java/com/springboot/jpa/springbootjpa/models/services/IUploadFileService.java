package com.springboot.jpa.springbootjpa.models.services;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public interface IUploadFileService {
    String copy(MultipartFile file) throws IOException;
    boolean delete(String filename);
    public void deleteAll();
    public void init() throws IOException;
}
