package xyz.st.meethere.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;
import xyz.st.meethere.entity.Ground;

import java.util.List;

@Repository
public interface GroundMapper {
//    用户场馆查询
    @Select("select * from ground")
    List<Ground> getAllGrounds();

    @Select("select * from ground where groundId=#{id}")
    Ground getGroundByGroundId(Integer id);

//    管理员场馆操作
    @Insert("insert into ground()")
    Ground addGround(Ground ground);
}
