package com.instagram.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.io.File;

@Service
public class FileUploadService {

    public void fileUpload(MultipartFile file, String fileName, String folderName)throws Exception{
        file.transferTo(new File("C:\\Users\\sg241\\IdeaProjects\\" + folderName + "\\" + fileName));
    }
}
