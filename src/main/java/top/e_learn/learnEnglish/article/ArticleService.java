package top.e_learn.learnEnglish.article;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.transaction.Transactional;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import top.e_learn.learnEnglish.category.Category;
import top.e_learn.learnEnglish.category.CategoryService;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@Data
public class ArticleService {

    private final ArticleRepository articleRepository;

    private final CategoryService categoryService;

    public List<Article> findAllArticlesFromCategory(long categoryId) {
        return articleRepository.findAllByCategoryId(categoryId);
    }

    @Transactional
    public Article getArticleByUuid(String uuid) {
        return articleRepository.findArticleByUuid(uuid).orElseThrow(() -> new ObjectNotFoundException("Article with id: " + uuid + "not found"));
    }

    public Article getNewArticle(String uuid) {
        return new Article(uuid, "Enter h1 new Article", "Enter description new Article");
    }

    public Page<Article> getArticlesPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return articleRepository.findAll(pageable);
    }

    @Transactional
    public void saveArticle(Article articleDb, Article article) {

        Optional.ofNullable(article.getH1()).ifPresent(articleDb::setH1);
        Optional.ofNullable(article.getDescription()).ifPresent(articleDb::setDescription);
        Optional.ofNullable(article.getHtmlTagDescription()).ifPresent(articleDb::setHtmlTagDescription);
        Optional.ofNullable(article.getHtmlTagTitle()).ifPresent(articleDb::setHtmlTagTitle);
        articleDb.setPublished(article.isPublished());
        if(article.getCategory() != null) {
            if(articleDb.getCategory() == null || !articleDb.getCategory().getUuid().equals(article.getCategory().getUuid())) {
            Category categoryByUuid = categoryService.getCategoryByUuid(article.getCategory().getUuid());
            articleDb.setCategory(categoryByUuid);
            }
        }
        Optional.ofNullable(article.getImage().getImageName()).ifPresent(imageName -> articleDb.getImage().setImageName(imageName));

        articleRepository.save(articleDb);
    }

    @Transactional
    public void saveNewArticle(Article article) {
        if(article.getCategory() != null) {
            Category categoryByUuid = categoryService.getCategoryByUuid(article.getCategory().getUuid());
            article.setCategory(categoryByUuid);
        }
        articleRepository.save(article);
    }


}
