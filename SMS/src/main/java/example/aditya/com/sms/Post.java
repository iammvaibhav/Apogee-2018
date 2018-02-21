package example.aditya.com.sms;

/**
 * Created by aditya on 2/14/2018.
 */

class Post {
    String title;
    String desc;

    public Post(String title, String desc) {
        this.title = title;
        this.desc = desc;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
