package top.e_learn.learnEnglish.payload.response;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import lombok.Getter;
import lombok.Setter;
import top.e_learn.learnEnglish.category.Category;

import java.util.List;

@Getter
@Setter
public class GetEntityAndMainCategoriesResponse<T> {

    private T t;

    private List<Category> mainCategories;

    public GetEntityAndMainCategoriesResponse(T t, List<Category> mainCategories) {
        this.t = t;
        this.mainCategories = mainCategories;
    }

}
