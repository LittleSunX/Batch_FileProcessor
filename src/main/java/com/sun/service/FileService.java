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

@Service
public class FileService {

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
            int lineNumber = 0; // 新添加的行数计数器
            while ((line = reader.readLine()) != null) {
                lineNumber++; // 每次读取新的一行，计数器加一
                if (END_OF_FILE.equals(line.trim())) {
                    break;
                }

                String[] fields = line.split("\\|");
                if (fields.length != 3) {
                    logger.error("文件第" + lineNumber + "行存在错误数据: {}", line);
                    continue;
                }

                User user = new User();
                user.setName(fields[0].trim());
                user.setAge(Integer.parseInt(fields[1].trim()));
                user.setGender(fields[2].trim());
                userMapper.insertOrUpdate(user);
            }
            logger.info("用户文件入库成功");
        } catch (Exception e) {
            logger.error("处理文件失败: " + filePath, e);
            logger.error("批量处理失败，请检查！");
            // 处理失败，结束程序
            System.exit(0);
        }
    }


}
