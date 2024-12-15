package kr.ac.baekseok.recyclehelper.Data;

public class SaleItem {
    private String name;
    private String description;
    private int price;
    private String code;

    public SaleItem() {}

    public SaleItem(String _name, String _description, int _price, String _code) {
        this.name = _name;
        this.description = _description;
        this.price = _price;
        this.code = _code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}