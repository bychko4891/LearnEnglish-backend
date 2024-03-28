package top.e_learn.learnEnglish.utils.dto;

import top.e_learn.learnEnglish.model.ApplicationPage;
import top.e_learn.learnEnglish.model.ApplicationPageContent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@Component
public class DtoTextOfAppPage {

    private ApplicationPageContent applicationPageContent;
//        private Long pageApplicationId;

    private ApplicationPage selectedApplicationPage;

    public DtoTextOfAppPage() {
    }

//    public TextOfAppPage getTextOfAppPage() {
//        return textOfAppPage;
//    }
//
//    public void setTextOfAppPage(TextOfAppPage textOfAppPage) {
//        this.textOfAppPage = textOfAppPage;
//    }

//    public Long getPageApplicationId() {
//        return pageApplicationId;
//    }

//    public void setPageApplicationId(Long pageApplicationId) {
//        this.pageApplicationId = pageApplicationId;
//    }


    public ApplicationPage getSelectedApplicationPage() {
        return selectedApplicationPage;
    }

    public void setSelectedApplicationPage(ApplicationPage selectedApplicationPage) {
        this.selectedApplicationPage = selectedApplicationPage;
    }
}
