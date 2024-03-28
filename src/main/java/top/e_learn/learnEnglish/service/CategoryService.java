package top.e_learn.learnEnglish.service;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import jakarta.transaction.Transactional;
import top.e_learn.learnEnglish.utils.exception.ObjectNotFoundException;
import top.e_learn.learnEnglish.model.Category;
import top.e_learn.learnEnglish.model.CategoryPage;
import top.e_learn.learnEnglish.repository.CategoryRepository;
import top.e_learn.learnEnglish.responsemessage.Message;
import top.e_learn.learnEnglish.responsemessage.CustomResponseMessage;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category getCategoryById(long id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();

        } else throw new ObjectNotFoundException("Category with id: " + id + "not found");
    }

    public Category getCategoryByUuid(String uuid) {
        Optional<Category> categoryOptional = categoryRepository.findCategoriesByUuid(uuid);
        if (categoryOptional.isPresent()) {
            return categoryOptional.get();

        } else throw new ObjectNotFoundException("Category with id: " + uuid + "not found");
    }

    public Category getNewCategory(String uuid) {
        Category category = new Category();
        category.setUuid(uuid);
        category.setName("Enter name new category");
        category.setDescription("Enter description new category");
        return category;
    }

    @Transactional
    public List<Category> getAllCategories() {
        return categoryRepository.getAllByMainCategories();
    }


    public List<Category> mainCategoryList(boolean mainCategory) {
        return categoryRepository.findCategoriesByMainCategoryOrderByNameAsc(mainCategory);
    }

    public List<Category> getMainCategoryListByCategoryPage(boolean mainCategory, CategoryPage categoryPage) {
        return categoryRepository.findCategoriesByMainCategoryAndCategoryPageOrderByNameAsc(mainCategory, categoryPage);
    }

    public List<Category> mainTranslationPairsCategoryListUser(boolean mainCategory) {
        return categoryRepository.findCategoriesByMainCategoryAndCategoryPageOrderByIdAsc(mainCategory, CategoryPage.MINI_STORIES);
    }

    public List<Category> getSubcategoriesFromMainCategory(String uuid) {
        return categoryRepository.findCategoriesByParentCategory_UuidOrderByNameAsc(uuid);
    }

    public List<Category> getSubcategoriesPhraseLesson() {
        List<Category> phraseLessonMainCategories = getMainCategoryListByCategoryPage(true, CategoryPage.LESSON_PHRASES);
        List<Category> subcategories = new ArrayList<>();
        for (Category arr: phraseLessonMainCategories) {
            subcategories.addAll(arr.getSubcategories());
        }
        return subcategories;
    }

    public CustomResponseMessage saveMainCategory(Category category, Category categoryDb) {
        categoryDb.setName(category.getName());
        categoryDb.setDescription(category.getDescription());
        categoryDb.setMainCategory(true);
        categoryDb.setShowDescriptionInPage(category.isShowDescriptionInPage());

        List<CategoryPage> categoryPages = new ArrayList<>(category.getCategoryPage());
        if (!categoryPages.isEmpty() && !categoryPages.get(0).equals(CategoryPage.NO_PAGE)) {
            categoryDb.getCategoryPage().clear();
            categoryDb.setCategoryPage(category.getCategoryPage());
        }
        if (categoryDb.getParentCategory() != null) { // ??????????
            Category parentCategory = categoryDb.getParentCategory();
            parentCategory.getSubcategories().removeIf(obj -> obj.getId().equals(categoryDb.getId()));
        }
        if(category.getImage().getImageName() != null) categoryDb.getImage().setImageName(category.getImage().getImageName());
        categoryDb.setParentCategory(null);
        categoryRepository.save(categoryDb);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }


    public CustomResponseMessage saveSubcategory(Category category, Category categoryDb) {
        categoryDb.setName(category.getName());
        categoryDb.setDescription(category.getDescription());
        categoryDb.setMainCategory(false);
        categoryDb.getCategoryPage().clear();
        if (categoryDb.getParentCategory() == null || !category.getParentCategory().getUuid().isBlank() && !category.getParentCategory().getUuid().equals(categoryDb.getParentCategory().getUuid())) {
            categoryDb.setParentCategory(getCategoryByUuid(category.getParentCategory().getUuid()));
        }
        if(category.getImage().getImageName() != null) categoryDb.getImage().setImageName(category.getImage().getImageName());
        categoryRepository.save(categoryDb);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }

    public CustomResponseMessage saveNewCategory(Category category) {
        if(category.getParentCategory() != null && !category.getParentCategory().getUuid().isBlank()) {
            Category parentCategory = getCategoryByUuid(category.getParentCategory().getUuid());
            category.setParentCategory(parentCategory);
        }
        categoryRepository.save(category);
        return new CustomResponseMessage(Message.ADD_BASE_SUCCESS);
    }
}
