package top.e_learn.learnEnglish.applicationPage;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import lombok.Data;
import org.springframework.stereotype.Service;
import top.e_learn.learnEnglish.applicationPage.applicationPageContent.ApplicationPageContent;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class ApplicationPageService {

    private final ApplicationPageRepository applicationPageRepository;

    public void savePageApplication(ApplicationPage applicationPage) {
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
        return applicationPageOptional.map(page -> page.getUuid().equals(applicationPage.getUuid())).orElse(false);
    }

    public void saveAppPage(ApplicationPage applicationPageDb, ApplicationPage applicationPage) {

    }

    public void saveNewAppPage(ApplicationPage applicationPage) {

    }

    public ApplicationPage getNewAppPage(String uuid) {
        return null;
    }
}
