package top.e_learn.learnEnglish.article;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class ArticleService {

    private final ArticleRepository articleRepository;

    public List<Article> findAllArticlesFromCategory(long categoryId) {
        return articleRepository.findAllByCategoryId(categoryId);
    }

    public List<Article> getAllArticles() {
        return (List<Article>)articleRepository.findAll();
    }
}
