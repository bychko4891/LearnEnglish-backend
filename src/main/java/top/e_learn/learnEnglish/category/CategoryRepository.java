package top.e_learn.learnEnglish.category;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface CategoryRepository extends CrudRepository<Category, Long> {
    @Query("SELECT MAX(c.id) FROM Category c")
    Long lastId();

    @Query("SELECT  c FROM Category c where c.parentCategory IS NULL")
    List<Category> getAllByMainCategories();

    List<Category> findCategoriesByMainCategoryOrderByNameAsc(boolean mainCategory);

    List<Category> findCategoriesByMainCategoryAndCategoryPageOrderByNameAsc(boolean mainCategory, CategoryPage categoryPage);

    List<Category> findCategoriesByMainCategoryAndCategoryPageOrderByIdAsc(boolean mainCategory, CategoryPage categoryPage);

    List<Category> findCategoriesByParentCategory_UuidOrderByNameAsc(String parentCategoryUuid);

    Optional<Category> findCategoryByUuid(String uuid);

}