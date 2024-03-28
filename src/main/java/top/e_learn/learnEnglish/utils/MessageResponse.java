package top.e_learn.learnEnglish.utils;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessageResponse {

    private String general;

    public MessageResponse(String general) {
        this.general = general;
    }
}
