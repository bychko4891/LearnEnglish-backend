package top.e_learn.learnEnglish.applicationPage;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import lombok.Data;
import org.springframework.stereotype.Service;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class ApplicationPageService {

    private final ApplicationPageRepository applicationPageRepository;

    public void saveNewAppPage(ApplicationPage applicationPage) {
        String url = applicationPage.getUrl().replace("^/|/$", "");
        applicationPage.setUrl(url);
        applicationPageRepository.save(applicationPage);
    }

    public ApplicationPage getAppPageById(long id) {
        return applicationPageRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("App page with id: " + id + "not found"));
    }

    public List<ApplicationPage> getAllAppPages() {
        return applicationPageRepository.getAllAppPages();
    }

    public ApplicationPage getAppPageByUuid(String uuid) {
        return applicationPageRepository.findApplicationPageByUuid(uuid)
                .orElseThrow(() -> new ObjectNotFoundException("No AppPage by UUID"));
    }

    public boolean existDuplicateUrl(ApplicationPage applicationPage) {
        Optional<ApplicationPage> applicationPageOptional = applicationPageRepository.findApplicationPageByUrl(applicationPage.getUrl());
        if(applicationPageOptional.isPresent()){
            return applicationPageOptional.map(page -> !page.getUuid().equals(applicationPage.getUuid())).orElse(true);
        }
        return false;
    }
    public ApplicationPage getApplicationPageByUrl(String url) {
        return applicationPageRepository.findApplicationPageByUrl(url)
                .orElseThrow(() -> new ObjectNotFoundException("No AppPage by UUID"));
    }

    public void saveAppPage(ApplicationPage applicationPageDb, ApplicationPage applicationPage) {
        String url = applicationPage.getUrl().replace("^/|/$", "");
        applicationPageDb.setUrl(url);
        Optional.ofNullable(applicationPage.getH1()).ifPresent(applicationPageDb::setH1);
        Optional.ofNullable(applicationPage.getHtmlTagTitle()).ifPresent(applicationPageDb::setHtmlTagTitle);
        Optional.ofNullable(applicationPage.getHtmlTagDescription()).ifPresent(applicationPageDb::setHtmlTagDescription);
        applicationPageRepository.save(applicationPageDb);
    }

    public ApplicationPage getNewAppPage(String uuid) {
       return new ApplicationPage(uuid, "h1 нової сторінки","унікальний url нової сторінки");
    }
}
