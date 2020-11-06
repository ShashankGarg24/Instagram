package com.instagram.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.Multipart;
import java.io.File;

@Service
public class FileUploadService {

    public String fileUpload(MultipartFile file, String fileName, String folderName)throws Exception{
        String path = "C:\\Users\\sg241\\IdeaProjects\\" + folderName + "\\" + fileName;
        file.transferTo(new File(path));
        return path;
    }
}
