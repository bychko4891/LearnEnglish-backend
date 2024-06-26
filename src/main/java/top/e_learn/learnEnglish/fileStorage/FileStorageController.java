package top.e_learn.learnEnglish.fileStorage;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.utils.exception.FileFormatException;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;

@RestController
@Data
@RequestMapping("api")
public class FileStorageController {

    @Value("${file.upload-audio}")
    private String audioStorePath;

    @Value("${file.upload-user-avatar}")
    private String userAvatarStorePath;

    @Value("${file.upload-category-image}")
    private String categoryStorePath;

    @Value("${file.upload-vocabulary-page-image}")
    private String vocabularyPageStorePath;

    @Value("${file.upload-web-image}")
    private String webImageStorePath;

    private final FileStorageService fileStorageService;

    @PostMapping("/admin/upload-web-img")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> uploadWebImageFromTinyMce(@RequestPart("imageFile") MultipartFile imageFile,
                                                 Principal principal) throws IOException {
        if (principal != null) {
            String contentType = imageFile.getContentType();
            if (contentType.equalsIgnoreCase("image/jpeg") || contentType.equalsIgnoreCase("image/png")
                    || contentType.equalsIgnoreCase("image/webp") || contentType.equalsIgnoreCase("image/jpg")) {
                String fileName = fileStorageService.storeFile(imageFile, webImageStorePath, "webImage");
                System.out.println(fileName);
                return ResponseEntity.ok(fileName);
            } else throw new FileFormatException("Дозволено тільки зображення");
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @GetMapping("/audio/{audioFileName:.+}")
    public ResponseEntity<Resource> getAudioFileFromStorage(@PathVariable String audioFileName) {
        Resource resource = fileStorageService.loadFileAsResource(audioFileName, audioStorePath);
        return ResponseEntity.ok(resource);
    }

    @GetMapping("/avatar/{imageFileName:.+}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String imageFileName) throws IOException {
        Resource resource = fileStorageService.loadFileAsResource(imageFileName, userAvatarStorePath);
        InputStream in = resource.getInputStream();
        byte[] imageBytes = IOUtils.toByteArray(in);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(imageBytes.length);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(resource.getFilename()).build());

        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/category-img/{imageFileName:.+}")
    public ResponseEntity<byte[]> categoryImage(@PathVariable String imageFileName) throws IOException {
        Resource resource = fileStorageService.loadFileAsResource(imageFileName, categoryStorePath);
        InputStream in = resource.getInputStream();
        byte[] imageBytes = IOUtils.toByteArray(in);
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG); // встановити тип контенту як image/jpeg, або image/png, залежно від формату зображення
        headers.setContentLength(imageBytes.length);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(resource.getFilename()).build());
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

    @GetMapping("/vocabulary-page/{imageFileName:.+}")
    public ResponseEntity<byte[]> vocabularyPageImage(@PathVariable String imageFileName) throws IOException {
        Resource resource = fileStorageService.loadFileAsResource(imageFileName,vocabularyPageStorePath);
        InputStream in = resource.getInputStream();
        byte[] imageBytes = IOUtils.toByteArray(in);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(imageBytes.length);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(resource.getFilename()).build());
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }


    @GetMapping("/webimg/{imageFileName:.+}")
    public ResponseEntity<byte[]> webImage(@PathVariable String imageFileName) throws IOException {
        Resource resource = fileStorageService.loadFileAsResource(imageFileName, webImageStorePath);
        InputStream in = resource.getInputStream();
        byte[] imageBytes = IOUtils.toByteArray(in);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentLength(imageBytes.length);
        headers.setContentDisposition(ContentDisposition.builder("inline").filename(resource.getFilename()).build());
        return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);
    }

}
