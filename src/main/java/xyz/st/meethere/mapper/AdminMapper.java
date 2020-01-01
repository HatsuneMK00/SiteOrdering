package xyz.st.meethere.mapper;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;
import xyz.st.meethere.entity.User;

@Repository
public interface AdminMapper {
    /**
     * @param userName
     * @return User
     */
    @Select("select * from user where userName=#{userName} and authority=1")
    User getAdminByName(String userName);

    /**
     * @param userId
     * @return User
     */
    @Select("select * from user where userId=#{userId} and authority=1")
    User getAdminById(int userId);


    /**
     * @param userName
     * @return User
     */
    @Select("select * from user where userName=#{userName}")
    User getUserByNameWOAuthority(String userName);

    /**
     * @param user
     * @return int
     */
    @Update("update user set userName=#{userName}"
            +
            ", password=#{password}"
            +
            ", email=#{email}"
            +
            ", description=#{description}"
            +
            ", profilePic=#{profilePic}"
            +
            ", balance=#{balance} "
            +
            "where userId=#{userId} and authority=1")
    int updateAdmin(User user);

    /**
     * @param userId
     * @param profilePic
     * @return int
     */
    @Update("update user set profilePic=#{profilePic} where userId=#{userId}")
    int updateAdminProfilePicByAdminId(Integer userId, String profilePic);

}
