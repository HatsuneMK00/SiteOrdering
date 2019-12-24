package xyz.st.meethere.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

@ApiModel("用户Model")
public class User {
    @ApiModelProperty("用户id")
    private int userId=0;
    @ApiModelProperty("用户名字-唯一")
    private String userName="";
    @ApiModelProperty("用户密码")
    private String password="";
    @ApiModelProperty("用户email")
    private String email="";
    @ApiModelProperty("用户签名")
    private String description="";
    @ApiModelProperty("用户头像")
    private String profilePic="";
    @ApiModelProperty("用户余额")
    private int balance=0;
    @ApiModelProperty("是否是管理员")
    private boolean authority=false;
    public User(){}

    public void updateUser(Map params) {
        if(params.containsKey("userId"))
            this.userId = Integer.valueOf(params.get("userId").toString());
        if(params.containsKey("userName"))
            this.userName = (String) params.get("userName");
        if(params.containsKey("password"))
            this.password = (String) params.get("password");
        if(params.containsKey("email"))
            this.email = (String) params.get("email");
        if(params.containsKey("description"))
            this.description = (String) params.get("description");
        if(params.containsKey("profilePic"))
            this.profilePic = (String) params.get("profilePic");
        if(params.containsKey("balance"))
            this.balance = (int) params.get("balance");
        if(params.containsKey("authority"))
            this.authority = (boolean) params.get("authority");

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean isAuthority() {
        return authority;
    }

    public void setAuthority(boolean authority) {
        this.authority = authority;
    }
}
