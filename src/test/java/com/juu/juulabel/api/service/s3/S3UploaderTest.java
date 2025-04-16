package com.juu.juulabel.api.service.s3;

import com.juu.juulabel.s3.S3Uploader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class S3UploaderTest {

    @Autowired
    private S3Uploader s3Uploader;

    @Test
    public void testConvertToWebpWithResize() throws Exception {
        // Given
        File testFile = new ClassPathResource("DSCF1381.JPG").getFile();
        MockMultipartFile multipartFile = new MockMultipartFile(
            "file",
            "DSCF1381.JPG",
            "image/jpeg",
            new FileInputStream(testFile)
        );
        String filePath = getFilePath("member");
        File originalFile = s3Uploader.convertMultipartToFile(multipartFile);

        // When
//        File convertedFile = s3Uploader.convertToWebpWithResize(originalFile, filePath);

        // Then
        double originalFileSizeKB = testFile.length() / 1024.0;
//        double convertedFileSizeKB = convertedFile.length() / 1024.0;

//        double compressionRate = 100 - (convertedFileSizeKB / originalFileSizeKB) * 100; // 압축률 계산

        System.out.printf("Original File Size: %.2f KB%n", originalFileSizeKB);
//        System.out.printf("Converted File Size: %.2f KB%n", convertedFileSizeKB);
//        System.out.printf("Compression Rate: %.2f%%%n", compressionRate);

//        assertTrue(compressionRate > 0, "압축률이 0% 이상이어야 합니다.");
//        Files.deleteIfExists(convertedFile.toPath());
    }

    private String getFilePath(String category) {
        return category + File.separator + createDatePath() + File.separator + generateRandomFilePrefix() + ".webp";
    }

    private String createDatePath() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    private String generateRandomFilePrefix() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }
}