package top.e_learn.learnEnglish.applicationPage.applicationPageContent;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.e_learn.learnEnglish.applicationPage.ApplicationPage;
import top.e_learn.learnEnglish.applicationPage.ApplicationPageService;
import top.e_learn.learnEnglish.image.Image;
import top.e_learn.learnEnglish.payload.response.GetAppPageContentResponse;
import top.e_learn.learnEnglish.fileStorage.FileStorageService;
import top.e_learn.learnEnglish.utils.MessageResponse;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("api")
@Data
public class ApplicationPageContentController {

    @Value("${file.upload-web-image}")
    private String webImageStorePath;

    private final ApplicationPageContentService applicationPageContentService;

    private final ApplicationPageService applicationPageService;

    private final FileStorageService fileStorageService;

    private final MessageSource messageSource;

    @GetMapping("/admin/app-pages-contents")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAllAppPagesContents(Principal principal) {
        if (principal != null) {
            List<ApplicationPageContent> applicationPageContentList = applicationPageContentService.getAllAppPagesContents();
            return ResponseEntity.ok(applicationPageContentList);
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @GetMapping("/admin/app-pages-contents/new-page-content")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<String> newAppPageContent(Principal principal) {
        if (principal != null) {
            return ResponseEntity.ok(UUID.randomUUID().toString());
        }
        return ResponseEntity.status(403).body("Access denied");
    }


    @GetMapping("/admin/app-pages-contents/page-content/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAppPageContent(@PathVariable String uuid, Principal principal) {
        if (principal != null) {
            List<ApplicationPage> applicationPages = applicationPageService.getAllAppPages();
            try {
                ApplicationPageContent applicationPageContent = applicationPageContentService.getAppPageContentByUuid(uuid);
                return ResponseEntity.ok(new GetAppPageContentResponse(applicationPageContent, applicationPages));
            } catch (ObjectNotFoundException e) {
                ApplicationPageContent applicationPageContent = applicationPageContentService.getNewAppPageContent(uuid);
                return ResponseEntity.ok(new GetAppPageContentResponse(applicationPageContent, applicationPages));
            }
        }
        return ResponseEntity.status(403).body("Access denied");
    }

    @PutMapping("/admin/app-pages-contents/page-content/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> saveAppPageContent(@PathVariable String uuid,
                                               @RequestPart(value = "image", required = false) MultipartFile imageFile,
                                               @RequestPart("pageContent") ApplicationPageContent applicationPageContent,
                                               Principal principal) throws IOException {
        if (principal != null) {
            Locale currentLocale = LocaleContextHolder.getLocale();
            try {
                ApplicationPageContent applicationPageContentDb = applicationPageContentService.getAppPageContentByUuid(uuid);
                applicationPageContent.setImage(new Image());
                if (imageFile != null) {
                    applicationPageContent.getImage().setImageName(fileStorageService.storeFile(imageFile, webImageStorePath, ""));
                    if (applicationPageContentDb.getImage().getImageName() != null)
                        fileStorageService.deleteFileFromStorage(applicationPageContentDb.getImage().getImageName(), webImageStorePath);
                }
                applicationPageContentService.saveAppPageContent(applicationPageContentDb, applicationPageContent);
                return ResponseEntity.ok(new MessageResponse( messageSource.getMessage("entity.save.success", null, currentLocale)));
            } catch (ObjectNotFoundException e) {
                Image image = new Image();
                if (imageFile != null) {
                    image.setImageName(fileStorageService.storeFile(imageFile, webImageStorePath, ""));
                }
                applicationPageContent.setImage(image);
                applicationPageContentService.saveNewAppPageContent(applicationPageContent);
                return ResponseEntity.ok(new MessageResponse( messageSource.getMessage("entity.save.success", null, currentLocale)));
            }
        }
        return ResponseEntity.status(403).body("Access denied");
    }

}
