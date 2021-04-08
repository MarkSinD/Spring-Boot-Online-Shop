package com.shopme.shopmebackend;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * DECRIPTION
 *
 * @author Mark Sinakaev
 * @version 1.0
 */
public class FileUploadUtil {
    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if(!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        System.out.println(uploadPath.toString());
        System.out.println(Files.exists(uploadPath));


        try(InputStream inputStream = multipartFile.getInputStream()){
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex){
            throw new IOException("Could not save file: " + fileName, ex);
        }
    }

    public static void cleanDir(String dir){
        Path dirPath = Paths.get(dir);
        try{
            Files.list(dirPath).forEach(file -> {
                if(!Files.isDirectory(file)){
                    try{
                        Files.delete(file);
                    } catch (IOException e) {
                        System.out.println("Could not delete file : " + file);
                    }
                }
            });
        }catch (IOException ex){
            System.out.println("Could not list directory: " + dirPath);
        }
    }
}
