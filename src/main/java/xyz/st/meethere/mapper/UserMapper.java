package xyz.st.meethere.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import xyz.st.meethere.entity.User;

import java.util.List;

@Repository
public interface UserMapper {
    @Select("select * from user where userName=#{userName} and authority=0")
    User getUserByName(String userName);

    @Select("select * from user where userName=#{userName}")
    User getUserByNameWOAuthority(String userName);

    @Select("select * from user where authority=0")
    List<User> getAllUser();

    @Select("select * from user where userId=#{userId} and authority=0")
    User getUserById(int userId);

    @Options(useGeneratedKeys = true,keyProperty = "userId")
    @Insert("insert into user(userName,password,email,description,profilePic,balance,authority) values(#{userName},#{password},#{email},#{description},#{profilePic},#{balance},0)")
    int addUser(User user);

    @Update("update user set userName=#{userName}"+
            ", password=#{password}" +
            ", email=#{email}" +
            ", description=#{description}" +
            ", profilePic=#{profilePic}" +
            ", balance=#{balance} "+
            "where userId=#{userId} and authority=0")
    int updateUser(User user);

    @Delete("delete from user where userId=#{userId} and authority=0")
    int deleteUserById(Integer userId);

    @Update("update user set profilePic=#{profilePic} where userId=#{userId}")
    int updateUserProfilePicByUserId(Integer userId, String profilePic);

}
