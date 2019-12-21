package xyz.st.meethere.mapper;

import org.apache.ibatis.annotations.*;
import xyz.st.meethere.entity.User;

import java.util.List;

public interface AdminMapper {
    @Select("select * from user where userName=#{userName} and authority=1")
    User getAdminByName(String userName);

    @Select("select * from user where userId=#{userId} and authority=1")
    User getAdminById(int userId);

    @Select("select * from user where userName=#{userName}")
    User getUserByNameWOAuthority(String userName);

    @Update("update user set userName=#{userName}"+
            ", password=#{password}" +
            ", email=#{email}" +
            ", description=#{description}" +
            ", profilePic=#{profilePic}" +
            ", balance=#{balance} "+
            "where userId=#{userId} and authority=1")
    int updateAdmin(User user);

    @Update("update user set profilePic=#{profilePic} where userId=#{userId}")
    int updateAdminProfilePicByAdminId(Integer userId, String profilePic);

}
