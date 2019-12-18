package xyz.st.meethere.entity;

import java.sql.Timestamp;

public class PreOrder {
    private int preOrderId;
    private int groundId;
    private int userId;
    private Timestamp orderTime;
    private int price;
    private String startTime;
    private int duration;
    private int payed;

    public int getPreOrderId() {
        return preOrderId;
    }

    public void setPreOrderId(int preOrderId) {
        this.preOrderId = preOrderId;
    }

    public int getGroundId() {
        return groundId;
    }

    public void setGroundId(int groundId) {
        this.groundId = groundId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getPayed() {
        return payed;
    }

    public void setPayed(int payed) {
        this.payed = payed;
    }
}
