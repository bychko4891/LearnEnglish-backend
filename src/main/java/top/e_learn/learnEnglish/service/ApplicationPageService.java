package top.e_learn.learnEnglish.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.e_learn.learnEnglish.repository.ApplicationPageRepository;

@Service
@AllArgsConstructor
public class ApplicationPageService {

    private final ApplicationPageRepository applicationPageRepository;

}
