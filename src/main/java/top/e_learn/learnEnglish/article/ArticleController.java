package top.e_learn.learnEnglish.article;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import lombok.Data;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
@Data
public class ArticleController {
}
