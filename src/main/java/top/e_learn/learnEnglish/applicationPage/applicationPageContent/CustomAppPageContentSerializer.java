package top.e_learn.learnEnglish.applicationPage.applicationPageContent;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import top.e_learn.learnEnglish.applicationPage.ApplicationPage;

import java.io.IOException;

public class CustomAppPageContentSerializer extends JsonSerializer<ApplicationPageContent> {

    @Override
    public void serialize(ApplicationPageContent value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("uuid", value.getUuid());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("description", value.getDescription());
        gen.writeObjectField("image", value.getImage());
        gen.writeObjectField("positionContent", value.getPositionContent());
        if (value.getApplicationPage() != null) {
            gen.writeFieldName("applicationPage");
            serializeAppPage(value.getApplicationPage(), gen, serializers);
        }
        gen.writeEndObject();
    }

    private void serializeAppPage(ApplicationPage value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("uuid", value.getUuid());
        gen.writeStringField("h1", value.getH1());
        gen.writeStringField("htmlTagDescription", value.getHtmlTagDescription());
        gen.writeStringField("htmlTagTitle", value.getHtmlTagTitle());
        gen.writeStringField("url", value.getUrl());
        gen.writeEndObject();
    }

}