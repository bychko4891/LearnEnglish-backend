package top.e_learn.learnEnglish.applicationPage;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 * GitHub source code: https://github.com/bychko4891/learnenglish
 */

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import top.e_learn.learnEnglish.applicationPage.applicationPageContent.ApplicationPageContent;

import java.io.IOException;

public class CustomAppPageSerializer extends JsonSerializer<ApplicationPage> {

    @Override
    public void serialize(ApplicationPage value, JsonGenerator gen, SerializerProvider serializers) throws IOException {

        gen.writeStartObject();
        if(value.getId() != null) gen.writeNumberField("id", value.getId());
        gen.writeStringField("uuid", value.getUuid());
        gen.writeStringField("h1", value.getH1());
        gen.writeStringField("htmlTagDescription", value.getHtmlTagDescription());
        gen.writeStringField("htmlTagTitle", value.getHtmlTagTitle());
        gen.writeStringField("url", value.getUrl());

        if (value.getAppPageContents() != null && !value.getAppPageContents().isEmpty()) {
            gen.writeFieldName("appPageContents");
            gen.writeStartArray();
            for (ApplicationPageContent pageContent : value.getAppPageContents()) {
                serializeAppPageContents(pageContent, gen, serializers);
            }
            gen.writeEndArray();
        }
        gen.writeEndObject();
    }

    private void serializeAppPageContents(ApplicationPageContent value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeStringField("uuid", value.getUuid());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("description", value.getDescription());
        gen.writeNumberField("order", value.getOrder());
        gen.writeObjectField("image", value.getImage());
        gen.writeObjectField("positionContent", value.getPositionContent());
        gen.writeEndObject();
    }


}