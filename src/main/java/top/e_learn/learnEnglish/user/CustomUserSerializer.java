package top.e_learn.learnEnglish.user;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class CustomUserSerializer extends JsonSerializer<User> {

    private static final int MAX_DEPTH = 3;

    @Override
    public void serialize(User value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        serializeCategory(value, gen, serializers, 0);

    }

    private void serializeCategory(User value, JsonGenerator gen, SerializerProvider serializers, int depth) throws IOException {

        if (depth >= MAX_DEPTH) {
            gen.writeNull();
            return;
        }

        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("uuid", value.getUuid());
        gen.writeStringField("login", value.getLogin());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("email", value.getEmail());
        gen.writeBooleanField("enable", value.isEnable());
        gen.writeStringField("userIp", value.getUserIp());
        gen.writeStringField("about", value.getAbout());
        gen.writeObjectField("dateOfCreated", value.getDateOfCreated());
        gen.writeObjectField("lastVisit", value.getLastVisit());
        gen.writeObjectField("userAvatar", value.getUserAvatar());
        gen.writeObjectField("userRole", value.getUserRole());
        gen.writeObjectField("gender", value.getGender());


//        if(value.getParentCategory() != null) {
//            gen.writeFieldName("parentCategory");
//            serializeParentCategory(value.getParentCategory(), gen, serializers);
//        }
//
//        if (value.getSubcategories() != null && !value.getSubcategories().isEmpty()) {
//            gen.writeFieldName("subcategories");
//            gen.writeStartArray();
//            for (Category subcategory : value.getSubcategories()) {
//                serializeCategory(subcategory, gen, serializers, depth + 1);
//            }
//            gen.writeEndArray();
//        }
        gen.writeEndObject();
    }

//    private void serializeParentCategory(Category value, JsonGenerator gen, SerializerProvider serializers) throws IOException{
//        gen.writeStartObject();
//        gen.writeStringField("uuid", value.getUuid());
//        gen.writeStringField("name", value.getName());
//        gen.writeStringField("description", value.getDescription());
//        gen.writeStringField("htmlTagTitle", value.getHtmlTagTitle());
//        gen.writeStringField("htmlTagDescription", value.getHtmlTagDescription());
//        gen.writeBooleanField("mainCategory", value.isMainCategory());
//        gen.writeObjectField("categoryPage", value.getCategoryPage());
//        gen.writeObjectField("image", value.getImage());
//        gen.writeEndObject();
//    }
}