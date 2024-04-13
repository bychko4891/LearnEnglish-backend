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
public class GetArticleResponse {

    private Article article;

    private List<Category> mainCategories;

    public GetArticleResponse(Article article, List<Category> mainCategories) {
        this.article = article;
        this.mainCategories = mainCategories;
    }

}