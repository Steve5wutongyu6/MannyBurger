package com.wutongyu.mannyburger;

//订单信息封装类
public class Order {
    private int id;
    private String items;
    private String note;
    private double totalPrice;

    public Order() {
        super();
    }

    public Order(int id, String items, String note, double totalPrice) {
        this.id = id;
        this.items = items;
        this.note = note;
        this.totalPrice = totalPrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", items='" + items + '\'' +
                ", note='" + note + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
