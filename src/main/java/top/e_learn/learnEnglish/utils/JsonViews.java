package top.e_learn.learnEnglish.utils;

public final class JsonViews {
    public interface ViewFieldUUID {}
    public interface ViewFieldLogin {}
    public interface ViewFieldName{}
    public interface ViewFieldEmail{}
    public interface ViewFieldGender{}
    public interface ViewFieldUserAbout{}
    public interface ViewFieldDateCreate{}
    public interface ViewFieldLastVisit{}
    public interface ViewFieldAuthority{}
    public interface ViewFieldJWT{}
    public interface ViewFieldUser{}

    public interface ViewWordId{}


    public interface ViewFieldOther{}
    public interface ViewFieldImage{}
    public interface ViewFieldWord{}
    public interface ViewFieldAudio{}

    public interface ViewUserProfile extends ViewFieldUUID, ViewFieldName, ViewFieldGender, ViewFieldImage, ViewFieldLogin,
            ViewFieldEmail, ViewFieldUserAbout, ViewFieldDateCreate, ViewFieldLastVisit, ViewFieldAuthority{}

    public interface ViewIdAndName extends ViewWordId, ViewFieldUUID, ViewFieldName{}
    public interface ViewAllCategories extends ViewFieldUUID, ViewFieldName, ViewFieldOther{}
    public interface ViewWordForWordLesson extends ViewFieldUUID, ViewFieldName, ViewFieldOther, ViewFieldWord, ViewFieldImage, ViewFieldAudio{}

}
