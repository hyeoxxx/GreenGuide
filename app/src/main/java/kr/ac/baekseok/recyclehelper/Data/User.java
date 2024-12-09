package kr.ac.baekseok.recyclehelper.Data;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/*
    작성자 최혁 (griscaf@gmail.com)
 */
public class User {
    private String email;
    private String nickname;
    private int point;
    @ServerTimestamp
    private Date timestamp;

    public User() {
    }
    public User(String _email, String _nickname, int _point) {
        this.email = _email;
        this.nickname = _nickname;
        this.point = _point;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String _email) {
        email = _email;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String _name) {
        nickname = _name;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int _point) {
        point = _point;
    }
    public void gainPoint(int amount) {

        point += amount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

}
