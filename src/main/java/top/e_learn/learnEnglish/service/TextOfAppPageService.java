package top.e_learn.learnEnglish.service;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.model.ApplicationPageContent;
import top.e_learn.learnEnglish.utils.dto.DtoTextOfAppPage;
import top.e_learn.learnEnglish.model.ApplicationPage;
import top.e_learn.learnEnglish.repository.ApplicationPageContentRepository;
import top.e_learn.learnEnglish.responsemessage.Message;
import top.e_learn.learnEnglish.responsemessage.CustomResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TextOfAppPageService {
    private final ApplicationPageContentRepository applicationPageContentRepository;
    private final PageApplicationService pageApplicationService;

    public CustomResponseMessage textOfAppPageEdit(DtoTextOfAppPage dtoTextOfAppPage) {
        Optional<ApplicationPageContent> textOfAppPageOptional = applicationPageContentRepository.findById(dtoTextOfAppPage.getApplicationPageContent().getId());
        ApplicationPageContent applicationPageContent = dtoTextOfAppPage.getApplicationPageContent();
        if (textOfAppPageOptional.isPresent()) {
            ApplicationPageContent applicationPageContentBase = textOfAppPageOptional.get();
            applicationPageContentBase.setH1(applicationPageContent.getH1());
            applicationPageContentBase.setDescription(applicationPageContent.getDescription());
            if (dtoTextOfAppPage.getSelectedApplicationPage().getId() == 0) {
                if(applicationPageContentBase.getApplicationPage() != null && !dtoTextOfAppPage.getSelectedApplicationPage().getPageURL().isBlank()){
                    applicationPageContentBase.getApplicationPage().setPageURL(dtoTextOfAppPage.getSelectedApplicationPage().getPageURL());
                }
                applicationPageContentRepository.save(applicationPageContentBase);
                return new CustomResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
            } else {
                ApplicationPage applicationPage = pageApplicationService.getPageApplication(dtoTextOfAppPage.getSelectedApplicationPage().getId());
                if(searchTextOfAppPageByPageApplicationId(applicationPage.getId()).isEmpty() ||
                        applicationPageContentBase.getApplicationPage().getId().equals(applicationPage.getId())) {
                    if(!dtoTextOfAppPage.getSelectedApplicationPage().getPageURL().isBlank()){
                        applicationPage.setPageURL(dtoTextOfAppPage.getSelectedApplicationPage().getPageURL());
                    }
                    applicationPageContentBase.setApplicationPage(applicationPage);
                    applicationPageContentRepository.save(applicationPageContentBase);
                    return new CustomResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
                }else{
                    return new CustomResponseMessage(Message.ERROR_SAVE_TEXT_OF_PAGE);
                }
            }
        } else return saveNewTextOfAppPage(dtoTextOfAppPage);
    }

    private CustomResponseMessage saveNewTextOfAppPage(DtoTextOfAppPage dtoTextOfAppPage){
        ApplicationPageContent applicationPageContent = new ApplicationPageContent();
        applicationPageContent.setH1(dtoTextOfAppPage.getApplicationPageContent().getH1());
        applicationPageContent.setDescription(dtoTextOfAppPage.getApplicationPageContent().getDescription());
        if (dtoTextOfAppPage.getSelectedApplicationPage().getId() != 0) {
            if (searchTextOfAppPageByPageApplicationId(dtoTextOfAppPage.getSelectedApplicationPage().getId()).isEmpty()) {
                ApplicationPage applicationPage = pageApplicationService.getPageApplication(dtoTextOfAppPage.getSelectedApplicationPage().getId());
                applicationPage.setPageURL(dtoTextOfAppPage.getSelectedApplicationPage().getPageURL());
                applicationPageContent.setApplicationPage(applicationPage);
                applicationPageContentRepository.save(applicationPageContent);
                return new CustomResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
            } else {
                return new CustomResponseMessage(Message.ERROR_SAVE_TEXT_OF_PAGE);
            }
        } else {
            applicationPageContentRepository.save(applicationPageContent);
            return new CustomResponseMessage(Message.SUCCESS_SAVE_TEXT_OF_PAGE);
        }
    }

    private Optional<ApplicationPageContent> searchTextOfAppPageByPageApplicationId(Long  pageApplicationId){
        return applicationPageContentRepository.findApplicationPageContentByApplicationPageId(pageApplicationId);
    }

    public List<ApplicationPageContent> getAppTextPageList() {
        return (List<ApplicationPageContent>) applicationPageContentRepository.findAll();
    }

    public Long countTextOfAppPage() {
        return applicationPageContentRepository.count();
    }

    public ApplicationPageContent findByIdTextOfAppPage(Long id) {
        Optional<ApplicationPageContent> textOfAppPageOptional = applicationPageContentRepository.findById(id);
        if (textOfAppPageOptional.isPresent()) {
            return textOfAppPageOptional.get();
        } else throw new IllegalArgumentException("TextOfAppPage with id " + id + " not found");

    }

}
