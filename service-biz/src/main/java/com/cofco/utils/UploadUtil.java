package com.cofco.utils;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class UploadUtil {
    public static String uploadFile(MultipartFile file, String path) throws IOException {
        // 上传文件的真实名称
        String name = file.getOriginalFilename();
        // 获得后缀名
        String suffixName = name.substring(name.lastIndexOf("."));
        String hash = Integer.toHexString(new Random().nextInt());
        String fileName = hash + suffixName;
        File tempFile = new File(path, fileName);
        if (!tempFile.getParentFile().exists()) {
            tempFile.getParentFile().mkdir();
        }
        if (tempFile.exists()) {
            tempFile.delete();
        }
        tempFile.createNewFile();
        file.transferTo(tempFile);
        return tempFile.getName();
    }
}
