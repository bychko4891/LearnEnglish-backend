package top.e_learn.learnEnglish.utils;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ParserToResponseFromCustomFieldError {

    private Map<String, String> fieldErrors;


    public static Map<String, String> parseCustomFieldErrors(List<CustomFieldError> collection) {
        Map<String, String> fieldErrors = new HashMap<>();

        for (CustomFieldError arr : collection) {
            String fieldName = arr.getFieldName();
            String fieldMessage = arr.getFieldMessage();

            fieldErrors.merge(fieldName, fieldMessage, (existingValue, newValue) -> existingValue + ", " + newValue);
        }
        return fieldErrors;
    }
    public static Map<String, String> parseCustomFieldError(CustomFieldError error) {
        Map<String, String> fieldErrors = new HashMap<>();

        String fieldName = error.getFieldName();
        String fieldMessage = error.getFieldMessage();

        fieldErrors.merge(fieldName, fieldMessage, (existingValue, newValue) -> existingValue + ", " + newValue);
        return fieldErrors;
    }
}
