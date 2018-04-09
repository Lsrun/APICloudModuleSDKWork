package base.model;

/**
 * Created by yangshiyou on 2017/9/13.
 */

public class UserInfo {

//    {"code":200,"result":
// {"headimgurl":"\/Uploads\/Picture\/2017-09-26\/59c9d06ed6614.jpeg",
// "nickname":"aa","mobile":"15212341234",
// "cards":"65565656","address":"aaaafffdd",
// "balance":"0.00",
// "total_price":0,
// "day_total_price":0,
// "rate":"9.9"}}


    private int id;


    private int uid;

    private String mobile;

    private String username;

    private String nickname;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
