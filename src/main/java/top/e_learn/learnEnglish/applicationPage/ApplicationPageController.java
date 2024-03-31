package top.e_learn.learnEnglish.applicationPage;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.e_learn.learnEnglish.applicationPage.applicationPageContent.ApplicationPageContent;
import top.e_learn.learnEnglish.model.Image;
import top.e_learn.learnEnglish.payload.response.GetAppPageContentResponse;
import top.e_learn.learnEnglish.utils.CustomFieldError;
import top.e_learn.learnEnglish.utils.MessageResponse;
import top.e_learn.learnEnglish.utils.ParserToResponseFromCustomFieldError;
import top.e_learn.learnEnglish.utils.ValidateFields;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api")
@AllArgsConstructor
public class ApplicationPageController {

    private final ApplicationPageService applicationPageService;

    private final MessageSource messageSource;

    @GetMapping("/admin/app-pages")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAllAppPages(Principal principal) {
        if (principal != null) {
            List<ApplicationPage> applicationPageContentList = applicationPageService.getAllAppPages();
            return ResponseEntity.ok(applicationPageContentList);
        }
        return ResponseEntity.badRequest().body("Access denied");
    }

    @GetMapping("/admin/app-pages/page/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> getAppPageContent(@PathVariable String uuid, Principal principal) {
        if (principal != null) {
            try {
                ApplicationPage applicationPage = applicationPageService.getAppPageByUuid(uuid);
                return ResponseEntity.ok(applicationPage);
            } catch (ObjectNotFoundException e) {
                ApplicationPage applicationPage = applicationPageService.getNewAppPage(uuid);
                return ResponseEntity.ok(applicationPage);
            }
        }
        return ResponseEntity.badRequest().body("Access denied");
    }

    @PutMapping("/admin/app-pages/page/{uuid}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createAppTextPage(@PathVariable String uuid,
                                               @Valid @RequestBody ApplicationPage applicationPage,
                                               BindingResult bindingResult,
                                               Principal principal) {
        if (principal != null) {
            Locale currentLocale = LocaleContextHolder.getLocale();
            if (bindingResult.hasErrors()) {
                List<CustomFieldError> errorFields = new ArrayList<>();
                try {
                    errorFields = bindingResult.getFieldErrors().stream()
                            .map(fieldError -> new CustomFieldError(fieldError.getField(), messageSource.getMessage(fieldError.getDefaultMessage(), null, currentLocale)))
                            .collect(Collectors.toList());
                    return ResponseEntity.badRequest().body(ParserToResponseFromCustomFieldError.parseCustomFieldErrors(errorFields));
                } catch (NoSuchMessageException e) {
                    errorFields.clear();
                    return ResponseEntity.badRequest().body(ParserToResponseFromCustomFieldError.parseCustomFieldErrors(errorFields));
                }
            }
            if(applicationPageService.existDuplicateUrl(applicationPage)) {
                return ResponseEntity.badRequest().body(new MessageResponse(messageSource.getMessage("page.bad.url.duplicate", null, currentLocale)));
            }
            try {
                ApplicationPage applicationPageDb = applicationPageService.getAppPageByUuid(uuid);
                applicationPageService.saveAppPage(applicationPageDb, applicationPage);
                return ResponseEntity.ok(new MessageResponse( messageSource.getMessage("entity.save.success", null, currentLocale)));
            } catch (ObjectNotFoundException e) {
                applicationPageService.saveNewAppPage(applicationPage);
                return ResponseEntity.ok(new MessageResponse( messageSource.getMessage("entity.save.success", null, currentLocale)));
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("{url}")
    public ResponseEntity<?> getAppPage(@PathVariable String url) {

        return null;
    }


}
