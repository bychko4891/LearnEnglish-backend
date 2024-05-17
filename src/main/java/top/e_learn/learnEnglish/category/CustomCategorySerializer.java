package top.e_learn.learnEnglish.category;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import top.e_learn.learnEnglish.dictionaryPage.DictionaryPage;
import top.e_learn.learnEnglish.wordLesson.WordLesson;
import top.e_learn.learnEnglish.wordLessonCard.WordLessonCard;

import java.io.IOException;

public class CustomCategorySerializer extends JsonSerializer<Category> {

    private static final int MAX_DEPTH = 3;

    @Override
    public void serialize(Category value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        serializeCategory(value, gen, serializers, 0);

    }

    private void serializeCategory(Category value, JsonGenerator gen, SerializerProvider serializers, int depth) throws IOException {

        if (depth >= MAX_DEPTH) {
            gen.writeNull();
            return;
        }

        gen.writeStartObject();
        gen.writeStringField("uuid", value.getUuid());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("description", value.getDescription());
        gen.writeStringField("miniDescription", value.getMiniDescription());
        gen.writeNumberField("sortOrder", value.getSortOrder());
        gen.writeStringField("htmlTagTitle", value.getHtmlTagTitle());
        gen.writeStringField("htmlTagDescription", value.getHtmlTagDescription());
        gen.writeBooleanField("mainCategory", value.isMainCategory());
        gen.writeObjectField("categoryPage", value.getCategoryPage());
        gen.writeObjectField("image", value.getImage());
//        gen.writeObjectField("wordLessons", value.getWordLessons());
//        if (!value.getWordLessons().isEmpty()) {
//            gen.writeObjectField("wordLessons", value.getWordLessons());

            gen.writeFieldName("wordLessons");
            gen.writeStartArray();
            for (WordLesson wordLesson : value.getWordLessons()) {
                serializeWordLesson(wordLesson, gen, serializers);
            }
            gen.writeEndArray();
//        }

        if(value.getParentCategory() != null) {
            gen.writeFieldName("parentCategory");
            serializeParentCategory(value.getParentCategory(), gen, serializers);
        }

        if (value.getSubcategories() != null && !value.getSubcategories().isEmpty()) {
            gen.writeFieldName("subcategories");
            gen.writeStartArray();
            for (Category subcategory : value.getSubcategories()) {
                serializeCategory(subcategory, gen, serializers, depth + 1);
            }
            gen.writeEndArray();
        }




        gen.writeEndObject();
    }

    private void serializeParentCategory(Category value, JsonGenerator gen, SerializerProvider serializers) throws IOException{
        gen.writeStartObject();
        gen.writeStringField("uuid", value.getUuid());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("description", value.getDescription());
        gen.writeStringField("miniDescription", value.getMiniDescription());
        gen.writeNumberField("sortOrder", value.getSortOrder());
        gen.writeStringField("htmlTagTitle", value.getHtmlTagTitle());
        gen.writeStringField("htmlTagDescription", value.getHtmlTagDescription());
        gen.writeBooleanField("mainCategory", value.isMainCategory());
        gen.writeObjectField("categoryPage", value.getCategoryPage());
        gen.writeObjectField("image", value.getImage());
        if (!value.getWordLessons().isEmpty()) {
//            gen.writeObjectField("wordLessons", value.getWordLessons());

            gen.writeFieldName("wordLessons");
            gen.writeStartArray();
            for (WordLesson wordLesson : value.getWordLessons()) {
                serializeWordLesson(wordLesson, gen, serializers);
            }
            gen.writeEndArray();
        }
        gen.writeEndObject();
    }

    private void serializeWordLesson(WordLesson value, JsonGenerator gen, SerializerProvider serializers) throws IOException{
        gen.writeStartObject();
        gen.writeStringField("uuid", value.getUuid());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("description", value.getDescription());
        gen.writeNumberField("sortOrder", value.getSortOrder());
        if (!value.getCards().isEmpty()) {
            gen.writeFieldName("cards");
            gen.writeStartArray();
            for (WordLessonCard card : value.getCards()) {
                serializeWordLessonCard(card, gen, serializers);
            }
            gen.writeEndArray();
        }
//        gen.writeStringField("miniDescription", value.getMiniDescription());
//        gen.writeNumberField("sortOrder", value.getSortOrder());
//        gen.writeStringField("htmlTagTitle", value.getHtmlTagTitle());
//        gen.writeStringField("htmlTagDescription", value.getHtmlTagDescription());
//        gen.writeBooleanField("mainCategory", value.isMainCategory());
//        gen.writeObjectField("categoryPage", value.getCategoryPage());
//        gen.writeObjectField("image", value.getImage());
        gen.writeEndObject();
    }
    private void serializeWordLessonCard(WordLessonCard value, JsonGenerator gen, SerializerProvider serializers) throws IOException{
        gen.writeStartObject();
        gen.writeStringField("uuid", value.getUuid());
        gen.writeStringField("description", value.getDescription());
        gen.writeNumberField("sortOrder", value.getSortOrder());
        gen.writeEndObject();
    }
//    private void serializeDictionaryPage(DictionaryPage value, JsonGenerator gen, SerializerProvider serializers) throws IOException{
//        gen.writeStartObject();
//        gen.writeStringField("uuid", value.getUuid());
//        gen.writeStringField("description", value.getDescription());
//
//        gen.writeObjectField("word", value.getWord());
//        gen.writeEndObject();
//    }
}