package top.e_learn.learnEnglish.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomFieldError {
    private String fieldName;
    private String fieldMessage;


    public CustomFieldError(String fieldName, String fieldMessage) {
        this.fieldName = fieldName;
        this.fieldMessage = fieldMessage;
    }
}
