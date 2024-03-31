package top.e_learn.learnEnglish.payload.response;

import lombok.Getter;
import lombok.Setter;
import top.e_learn.learnEnglish.applicationPage.ApplicationPage;
import top.e_learn.learnEnglish.applicationPage.applicationPageContent.ApplicationPageContent;

import java.util.List;

@Getter
@Setter
public class GetAppPageContentResponse {

    private ApplicationPageContent applicationPageContent;

    private List<ApplicationPage> applicationPages;

    public GetAppPageContentResponse(ApplicationPageContent applicationPageContent, List<ApplicationPage> applicationPages) {
        this.applicationPageContent = applicationPageContent;
        this.applicationPages = applicationPages;
    }
}
