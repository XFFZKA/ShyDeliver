package com.sky.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.regex.Pattern;

@Component
public class LocalFileStorageUtil {
    // 允许的文件扩展名白名单
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            ".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp"
    );

    // 安全文件名正则（支持中文、字母、数字、下划线、连字符）
    private static final Pattern SAFE_FILENAME_PATTERN =
            Pattern.compile("^[\\w\\-\\u4e00-\\u9fa5]+(\\.[a-zA-Z0-9]+)$");

    @Value("${file.upload-dir:uploads/}")
    private String uploadDir;

    /*
     * 安全文件上传方法（保持原有接口不变）
     * @param bytes 文件字节数组
     * @param objectName 原始文件名
     * @return 文件访问路径
     */
    public String upload(byte[] bytes, String objectName) throws IOException {
        // 1. 输入验证
        validateInput(bytes, objectName);

        // 2. 构建安全存储路径
        Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path targetPath = buildSafePath(uploadPath, objectName);

        // 3. 原子化写入文件
        writeFileAtomically(bytes, targetPath);

        return "http://localhost:8080/files/" + objectName;
    }

    //=========== 安全增强方法 ===========//
    private void validateInput(byte[] bytes, String filename) {
        // 空文件检查
        if (bytes == null || bytes.length == 0) {
            throw new IllegalArgumentException("文件内容不能为空");
        }

        // 文件名格式检查
        if (!SAFE_FILENAME_PATTERN.matcher(filename).matches()) {
            throw new IllegalArgumentException("文件名包含非法字符: " + filename);
        }

        // 文件扩展名检查
        String extension = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("不支持的文件类型，仅允许: " + ALLOWED_EXTENSIONS);
        }
    }

    private Path buildSafePath(Path basePath, String filename) throws IOException {
        // 确保上传目录存在
        Files.createDirectories(basePath);

        // 解析目标路径并规范化
        Path targetPath = basePath.resolve(filename).normalize();

        // 防止路径穿越攻击
        if (!targetPath.startsWith(basePath)) {
            throw new SecurityException("检测到非法路径访问: " + filename);
        }

        return targetPath;
    }

    private void writeFileAtomically(byte[] bytes, Path targetPath) throws IOException {
        // 使用临时文件过渡
        Path tempFile = Files.createTempFile(targetPath.getParent(), "tmp_", ".upload");
        try {
            // 写入临时文件
            Files.write(tempFile, bytes);
            // 原子操作移动文件
            Files.move(tempFile, targetPath, StandardCopyOption.ATOMIC_MOVE);
        } catch (Exception e) {
            // 清理临时文件
            Files.deleteIfExists(tempFile);
            throw e;
        }
    }
}