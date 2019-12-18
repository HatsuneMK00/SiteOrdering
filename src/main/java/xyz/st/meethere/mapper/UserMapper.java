package xyz.st.meethere.mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import xyz.st.meethere.entity.User;

@Repository
public interface UserMapper {
    @Select("select * from user where userName=#{name}")
    User getUserByName(String name);
}
