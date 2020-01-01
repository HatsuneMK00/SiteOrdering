package xyz.st.meethere.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Ground")
public class Ground {
    @ApiModelProperty("场馆名字")
    private String groundName;
    @ApiModelProperty("场馆id号，是数据库中的主键，更新的时候需要传入，插入的时候不需要传入")
    private int groundId;
    @ApiModelProperty("场馆照片信息，不需要传入")
    private String photo;
    @ApiModelProperty("场馆每小时单价，要求为整数")
    private int pricePerHour;
    @ApiModelProperty("场馆地址")
    private String address;
    @ApiModelProperty("场馆描述")
    private String description;

    public Ground() {
    }

    public Ground(String groundName, int pricePerHour, String address, String description) {
        this.groundName = groundName;
        this.pricePerHour = pricePerHour;
        this.address = address;
        this.description = description;
    }

    public Ground(String groundName, int groundId, String photo, int pricePerHour, String address, String description) {
        this.groundName = groundName;
        this.groundId = groundId;
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

