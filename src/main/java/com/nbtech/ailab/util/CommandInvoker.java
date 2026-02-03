package com.nbtech.ailab.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author nber
 */
@Slf4j
public class CommandInvoker {

    private static final String LINUX = "linux";

    private static final String MAC = "mac";

    private static final String WINDOWS = "windows";

    /**
     * 运行本地命令行,获取输出结果
     *
     * @param command
     * @return
     * @throws Exception
     */
    public static String cmd(String command) throws Exception {

        log.info("command命令: {}", command);

        BufferedReader reader = null;
        StringBuilder cmdResult = new StringBuilder();

        ProcessBuilder processBuilder = null;
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains(LINUX) || osName.contains(MAC)) {
            processBuilder = new ProcessBuilder("bash", "-c", command);
//            processBuilder = new ProcessBuilder("python3", command);
            Map<String, String> environment = processBuilder.environment();
            environment.put("LANG", "en_US.UTF-8");
        } else if (osName.contains(WINDOWS)) {
            processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        } else {
            throw new RuntimeException("未知的操作系统");
        }
        // 执行命令
        Process process = processBuilder.start();
        // 获取命令输出流
        reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        String line;
        // 逐行读取输出结果
        while ((line = reader.readLine()) != null) {
            cmdResult.append(line);
            cmdResult.append("\n");
        }
        log.info("执行完毕: {}", cmdResult.toString());
        return cmdResult.toString();
    }

    /**
     * 运行本地命令行,获取输出结果
     *
     * @param command
     * @return
     * @throws Exception
     */
    public static String cmd2(String command) throws Exception {

        log.info("开始执行Python脚本 {}", command);

        BufferedReader reader = null;
        StringBuilder cmdResult = new StringBuilder();

        ProcessBuilder processBuilder = null;
        String osName = System.getProperty("os.name").toLowerCase();

        if (osName.contains(LINUX) || osName.contains(MAC)) {
//            processBuilder = new ProcessBuilder("bash", "-c", command);
            processBuilder = new ProcessBuilder("python3", command);
            Map<String, String> environment = processBuilder.environment();
            environment.put("LANG", "en_US.UTF-8");
        } else if (osName.contains(WINDOWS)) {
            processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        } else {
            throw new RuntimeException("未知的操作系统");
        }
        // 执行命令
        Process process = processBuilder.start();
        // 获取命令输出流
        reader = new BufferedReader(new InputStreamReader(process.getInputStream(), StandardCharsets.UTF_8));
        String line;
        // 逐行读取输出结果
        while ((line = reader.readLine()) != null) {
            cmdResult.append(line);
            cmdResult.append("\n");
        }
        log.info("python脚本执行完毕: {}", cmdResult.toString());
        return cmdResult.toString();
    }
}
