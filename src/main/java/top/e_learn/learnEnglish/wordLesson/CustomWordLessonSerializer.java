package top.e_learn.learnEnglish.wordLesson;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import top.e_learn.learnEnglish.wordLessonCard.WordLessonCard;

import java.io.IOException;

public class CustomWordLessonSerializer extends JsonSerializer<WordLesson> {

    private static final int MAX_DEPTH = 3;

    @Override
    public void serialize(WordLesson value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        serializeCategory(value, gen, serializers, 0);

    }

    private void serializeCategory(WordLesson value, JsonGenerator gen, SerializerProvider serializers, int depth) throws IOException {

        if (depth >= MAX_DEPTH) {
            gen.writeNull();
            return;
        }

        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("uuid", value.getUuid());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("description", value.getDescription());
        gen.writeNumberField("sortOrder", value.getSortOrder());
        gen.writeObjectField("category", value.getCategory());



        if (value.getCards() != null && !value.getCards().isEmpty()) {
            gen.writeFieldName("cards");
            gen.writeStartArray();
            for (WordLessonCard card : value.getCards()) {
                serializeParentCategory(card, gen, serializers);
            }
            gen.writeEndArray();
        }
        gen.writeEndObject();
    }

    private void serializeParentCategory(WordLessonCard value, JsonGenerator gen, SerializerProvider serializers) throws IOException{
        gen.writeStartObject();
        gen.writeStringField("uuid", value.getUuid());
        gen.writeStringField("description", value.getDescription());
        gen.writeObjectField("dictionaryPage", value.getDictionaryPage());
        gen.writeNumberField("sortOrder", value.getSortOrder());
//        gen.writeBooleanField("mainCategory", value.isMainCategory());
//        gen.writeObjectField("categoryPage", value.getCategoryPage());
//        gen.writeObjectField("image", value.getImage());
        gen.writeEndObject();
    }
}