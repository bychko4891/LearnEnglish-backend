package top.e_learn.learnEnglish.responsemessage;

/*
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

public class CustomResponseMessage {
    private String status;
    private String message;

    private String info;

    public CustomResponseMessage(Message message) {
        this.status = message.getStatus();
        this.message = message.getMessage();
    }
    public CustomResponseMessage(Message message, String info) {
        this.status = message.getStatus();
        this.info = info;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @Override
    public String toString() {
        return status + '\'' +
                ", message='" + message;
    }
}

