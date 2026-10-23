package com.example.studentmarket.service.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.UUID;

/** Lưu và xóa ảnh upload (dùng chung cho ảnh đại diện, ảnh tin đăng...). */
@Service
public class FileStorageService {

    private static final long MAX_SIZE = 2L * 1024 * 1024; // 2MB
    private static final Map<String, String> TYPES = Map.of(
            "image/jpeg", "jpg",
            "image/png", "png",
            "image/webp", "webp",
            "image/gif", "gif");

    private final Path root;

    public FileStorageService(@Value("${app.upload-dir:uploads}") String uploadDir) {
        this.root = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    /**
     * Lưu ảnh vào uploads/{subDir}/ với tên ngẫu nhiên.
     * @return đường dẫn để lưu vào database, ví dụ "/uploads/avatars/abc.jpg"
     * @throws IllegalArgumentException nếu file rỗng, quá lớn hoặc không phải ảnh
     */
    public String storeImage(MultipartFile file, String subDir) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Chưa chọn ảnh");
        }
        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("Ảnh quá lớn, tối đa 2MB");
        }
        String ext = TYPES.get(file.getContentType());
        if (ext == null) {
            throw new IllegalArgumentException("Chỉ nhận ảnh JPG, PNG, WEBP hoặc GIF");
        }
        try {
            Path dir = root.resolve(subDir).normalize();
            Files.createDirectories(dir);
            String name = UUID.randomUUID() + "." + ext;
            try (InputStream in = file.getInputStream()) {
                Files.copy(in, dir.resolve(name), StandardCopyOption.REPLACE_EXISTING);
            }
            return "/uploads/" + subDir + "/" + name;
        } catch (IOException e) {
            throw new UncheckedIOException("Không lưu được ảnh", e);
        }
    }

    /** Xóa file theo đường dẫn đã lưu trong database (bỏ qua nếu không tồn tại). */
    public void delete(String url) {
        if (url == null || !url.startsWith("/uploads/")) {
            return;
        }
        Path p = root.resolve(url.substring("/uploads/".length())).normalize();
        if (!p.startsWith(root)) { // chặn đường dẫn kiểu ../
            return;
        }
        try {
            Files.deleteIfExists(p);
        } catch (IOException ignored) {
            // không quan trọng nếu xóa file thất bại
        }
    }
}
