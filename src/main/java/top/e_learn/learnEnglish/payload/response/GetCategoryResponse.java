package top.e_learn.learnEnglish.payload.response;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import lombok.Getter;
import lombok.Setter;
import top.e_learn.learnEnglish.article.Article;
import top.e_learn.learnEnglish.category.Category;

import java.util.List;

@Getter
@Setter
public class GetCategoryResponse {

    private Category category;

    private List<Category> mainCategories;

    private List<Article> articles;

    public GetCategoryResponse(Category category, List<Category> mainCategories) {
        this.category = category;
        this.mainCategories = mainCategories;
    }

    public GetCategoryResponse(Category category, List<Article> articles, List<Category> mainCategories) {
        this.category = category;
        this.mainCategories = mainCategories;
        this.articles = articles;
    }
}
