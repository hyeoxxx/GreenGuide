package kr.ac.baekseok.recyclehelper.Data;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

/*
    작성자 최혁 (griscaf@gmail.com)
 */
public class Product {
    private String number;
    private String name;
    private String material;
    private boolean recyclable;
    private String category;
    @ServerTimestamp
    private Date timestamp;
    private String postId;

    public Product() {
        /*
        파이어베이스 사용할 때 필요?
         */
    }
    public Product(String _number, String _name, String _material, boolean _recyclable, String _category, String _postId) {
        this.number = _number;
        this.name = _name;
        this.material = _material;
        this.recyclable = _recyclable;
        this.category = _category;
        this.postId = _postId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String _number) {
        number = _number;
    }

    public String getName() {
        return name;
    }

    public void setName(String _name) {
        name = _name;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String _material) {
        material = _material;
    }
    public boolean getRecyclable() {
        return recyclable;
    }
    public void setRecyclable(boolean able) {
        recyclable = able;
    }
    public void setCategory(String _category) { category = _category; }
    public String getCategory() { return category; }

    public Date getTimestamp() {
        return timestamp;
    }
    public void setPostId(String id) {
        postId = id;
    }
    public String getPostId() {
        return postId;
    }
}
