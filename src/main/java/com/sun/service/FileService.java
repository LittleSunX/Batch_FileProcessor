package com.sun.service;

import com.sun.dao.UserMapper;
import com.sun.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileService {

    private static final int BATCH_SIZE = 1000; // 根据实际情况调整<EOF>
    private static final String END_OF_FILE = "<EOF>"; // 设置你的文件结束标识

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    private final UserMapper userMapper;

    public FileService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public void importUsersFromFile(String filePath) {
        Path path = Paths.get(filePath);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (END_OF_FILE.equals(line.trim())) {
                    break;
                }

                String[] fields = line.split("\\|");
                if (fields.length != 3) {
                    logger.error("无效行: {}", line);
                    continue;
                }

                User user = new User();
                user.setUid(UUID.randomUUID().toString());
                user.setName(fields[0].trim());
                user.setAge(Integer.parseInt(fields[1].trim()));
                user.setGender(fields[2].trim());
                userMapper.insertOrUpdate(user);
                logger.info("文件入库成功 user: {}", user.getUid());
            }
        } catch (Exception e) {
            // 处理失败，结束程序
            logger.error("处理文件失败: " + filePath, e);
            System.exit(0);
        }
    }


}