package com.sun;

import com.sun.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    private FileService fileService;

    @Autowired
    private ApplicationArguments args;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String date = "yyyyMMdd"; // 默认日期格式
        if (this.args.containsOption("d")) {
            date = this.args.getOptionValues("d").get(0); // 获取命令行参数
        } else {
            // 如果没有提供日期参数，则默认处理前一天的文件
            LocalDate yesterday = LocalDate.now().minusDays(1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
            date = formatter.format(yesterday);
        }
        String filePath = "D:" + File.separator + "test" + File.separator + date + File.separator + "file.txt"; // 根据日期处理对应目录下的文件
        fileService.importUsersFromFile(filePath);

        // 处理完文件后，结束程序
        System.exit(0);
    }
}
