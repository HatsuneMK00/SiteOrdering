package xyz.st.meethere.entity;

import java.util.Map;

public class Ground {
    private String groundName;
    private int groundId;
    private String photo;
    private int pricePerHour;
    private String address;
    private String description;

    public Ground() {
    }

    public Ground(Map params) {
        this.groundName = (String) params.get("groundName");
        this.photo = (String) params.get("photo");
        this.pricePerHour = (int) params.get("pricePerHour");
        this.address = (String) params.get("addreses");
        this.description = (String) params.get("description");
    }

    public Ground(String groundName, String photo, int pricePerHour, String address, String description) {
        this.groundName = groundName;
        this.photo = photo;
        this.pricePerHour = pricePerHour;
        this.address = address;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGroundName() {
        return groundName;
    }

    public void setGroundName(String groundName) {
        this.groundName = groundName;
    }

    public int getGroundId() {
        return groundId;
    }

    public void setGroundId(int groundId) {
        this.groundId = groundId;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(int pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
