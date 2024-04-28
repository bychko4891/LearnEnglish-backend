package top.e_learn.learnEnglish.image;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.image.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ImagesRepository extends JpaRepository<Image, Long> {

    @Query("SELECT i FROM Image i WHERE i.articleImage = :webImage ORDER BY i.id ASC")
    Page<Image> findAll(Pageable pageable, boolean webImage);

}
