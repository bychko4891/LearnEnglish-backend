package top.e_learn.learnEnglish;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import top.e_learn.learnEnglish.fileStorage.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.TimeZone;

@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class LearnEnglishApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Kiev"));
		SpringApplication.run(LearnEnglishApplication.class, args);
	}
}
