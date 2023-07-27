package com.sun;

import com.sun.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    private FileService fileService;

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... commandLineArgs) throws Exception {
        String date = "yyyyMMdd"; // 默认日期格式
        List<String> args = Arrays.asList(commandLineArgs);
        int dIndex = args.indexOf("-d");
        if (dIndex != -1 && dIndex < args.size() - 1) {
            date = args.get(dIndex + 1); // 获取命令行参数
            logger.info("命令行日期：" + date);
        } else {
            // 如果没有提供日期参数，则默认处理前一天的文件
            LocalDate yesterday = LocalDate.now().minusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            date = formatter.format(yesterday);
        }
        String filePath = "D:" + File.separator + "test" + File.separator + date + File.separator + "file.txt"; // 根据日期处理对应目录下的文件
        fileService.importUsersFromFile(filePath);
        logger.info("批量处理成功，结束批量");
        // 处理完文件后，结束程序
        System.exit(0);
    }
}

