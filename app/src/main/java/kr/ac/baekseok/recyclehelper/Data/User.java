package kr.ac.baekseok.recyclehelper.Data;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*
    작성자 최혁 (griscaf@gmail.com)
 */
public class User {
    private static User instance;
    private String email;
    private String nickname;
    private int point;
    private int rate;
    private List<SaleItem> inventory;

    @ServerTimestamp
    private Date timestamp;

    public User() {
    }

    public static User getInstance() {
        if (instance == null) {
            instance = new User();
        }
        return instance;
    }
    public User(String _email, String _nickname, int _point, int _rate, List<SaleItem> _inventory) {
        this.email = _email;
        this.nickname = _nickname;
        this.point = _point;
        this.rate = _rate;
        this.inventory = _inventory;
    }
    public void init(String _email, String _nickname, int _point, int _rate, List<SaleItem> _inventory) {
        this.email = _email;
        this.nickname = _nickname;
        this.point = _point;
        this.rate = _rate;
        this.inventory = _inventory;
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

    public int getRate() {
        return rate;
    }
    public void setRate(int amount) {
        rate = amount;
    }
    public void gainRate(int amount) {
        rate += amount;
    }

    public void setInventory(List<SaleItem> list) {
        inventory = list;
    }
    public void addInventory(SaleItem item) {
        inventory.add(item);
    }
    public List<SaleItem> getInventory() {
        if (inventory == null) {
            return new ArrayList<SaleItem>();
        }
        return inventory;
    }
}
