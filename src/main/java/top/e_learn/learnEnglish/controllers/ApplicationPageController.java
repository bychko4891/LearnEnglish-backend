package top.e_learn.learnEnglish.controllers;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class ApplicationPageController {

    private final top.e_learn.learnEnglish.service.ApplicationPageService ApplicationPageService;
}
