package top.e_learn.learnEnglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.model.ApplicationPage;
import top.e_learn.learnEnglish.repository.PageApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PageApplicationService {
    private final PageApplicationRepository pageApplicationRepository;

    public PageApplicationService(PageApplicationRepository pageApplicationRepository) {
        this.pageApplicationRepository = pageApplicationRepository;
    }

    public void savePageApplication(ApplicationPage applicationPage) {
        pageApplicationRepository.save(applicationPage);
    }

    public ApplicationPage getPageApplication(Long id) {
        Optional<ApplicationPage> pageApplicationOptional = pageApplicationRepository.findById(id);
        if (pageApplicationOptional.isPresent()) {
            return pageApplicationOptional.get();
        } else
            throw new RuntimeException("Error base in method getPageApplication() --> 'PageApplicationService.class' ");
    }

    public List<ApplicationPage> pageApplicationList() {
        return (List<ApplicationPage>) pageApplicationRepository.findAll();
    }
}
