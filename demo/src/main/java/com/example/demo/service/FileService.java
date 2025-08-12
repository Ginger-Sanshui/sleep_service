package com.example.demo.service;

import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Service
public class FileService {

    public List<String> readFileToList(String file) {
        try {
            // 读取 resources 目录下的文件（文件放在 src/main/resources/ 下）
            Path path = ResourceUtils.getFile(file).toPath();
            return Files.readAllLines(path, StandardCharsets.UTF_8);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}