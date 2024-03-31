package top.e_learn.learnEnglish.applicationPage.applicationPageContent;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.transaction.Transactional;
import top.e_learn.learnEnglish.applicationPage.ApplicationPage;
import top.e_learn.learnEnglish.applicationPage.ApplicationPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ApplicationPageContentService {

    private final ApplicationPageService applicationPageService;

    private final ApplicationPageContentRepository applicationPageContentRepository;

    @Transactional
    public ApplicationPageContent getAppPageContentByUuid(String uuid) {
        return applicationPageContentRepository.findApplicationPageContentByUuid(uuid)
                .orElseThrow(() -> new ObjectNotFoundException("No page content by UUID"));
    }

    public ApplicationPageContent getNewAppPageContent(String uuid) {
        ApplicationPageContent applicationPageContent = new ApplicationPageContent();
        applicationPageContent.setUuid(uuid);
        applicationPageContent.setDescription("Enter description new app page content");
        return applicationPageContent;
    }

    @Transactional
    public List<ApplicationPageContent> getAllAppPagesContents() {
        return applicationPageContentRepository.getAllApplicationPagesContents();
    }

    @Transactional
    public void saveAppPageContent(ApplicationPageContent applicationPageContentDB, ApplicationPageContent applicationPageContent) {
        Optional.ofNullable(applicationPageContent.getName()).ifPresent(applicationPageContentDB::setName);
        Optional.ofNullable(applicationPageContent.getDescription()).ifPresent(applicationPageContentDB::setDescription);
        Optional.ofNullable(applicationPageContent.getImage().getImageName())
                .ifPresent(imageName -> applicationPageContentDB.getImage().setImageName(imageName));

        if (applicationPageContent.getApplicationPage() != null) {
            ApplicationPage applicationPage = applicationPageService.getAppPageByUuid(applicationPageContent.getApplicationPage().getUuid());
            applicationPageContentDB.setApplicationPage(applicationPage);
        }
        if (!applicationPageContent.getPositionContent().isEmpty()) {//
            applicationPageContentDB.getPositionContent().clear();
            applicationPageContentDB.setPositionContent(applicationPageContent.getPositionContent());
        }
        applicationPageContentRepository.save(applicationPageContentDB);
//        return null;
    }

    @Transactional
    public void saveNewAppPageContent(ApplicationPageContent applicationPageContent) {
        if (applicationPageContent.getApplicationPage() != null) {
            ApplicationPage applicationPage = applicationPageService.getAppPageByUuid(applicationPageContent.getApplicationPage().getUuid());
            applicationPageContent.setApplicationPage(applicationPage);
        }
        applicationPageContentRepository.save(applicationPageContent);
    }

    private Optional<ApplicationPageContent> searchTextOfAppPageByPageApplicationId(Long pageApplicationId) {
        return applicationPageContentRepository.findApplicationPageContentByApplicationPageId(pageApplicationId);
    }


    public ApplicationPageContent findByIdTextOfAppPage(Long id) {
        Optional<ApplicationPageContent> textOfAppPageOptional = applicationPageContentRepository.findById(id);
        if (textOfAppPageOptional.isPresent()) {
            return textOfAppPageOptional.get();
        } else throw new IllegalArgumentException("TextOfAppPage with id " + id + " not found");

    }

}
