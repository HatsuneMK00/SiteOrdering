package xyz.st.meethere.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import xyz.st.meethere.entity.User;

@Repository
public interface UserMapper {
    @Select("select * from user where userName=#{name}")
    User getUserByName(String name);

    @Options(useGeneratedKeys = true,keyProperty = "userId")
    @Insert("insert into user(userName,password,profilePic) values(#{userName},#{password},#{profilePic})")
    int addUser(User user);

    @Update("update user set profilePic=#{profilePic} where userName=#{userName}")
    void updateUserProfilePic(User user);
}
