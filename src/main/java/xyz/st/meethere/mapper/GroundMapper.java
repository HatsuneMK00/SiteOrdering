package xyz.st.meethere.mapper;

import org.apache.ibatis.annotations.*;
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

    @Select("select * from ground where groundName like CONCAT('%',#{matchParam},'%')")
    List<Ground> getGroundsByGroundNameMatch(String matchParam);

    //    管理员场馆操作
    @Options(useGeneratedKeys = true,keyProperty = "groundId")
    @Insert("insert into ground(groundName,photo,pricePerHour,address,description) values(#{groundName},#{photo},#{pricePerHour}," +
            "#{address},#{description})")
    int addGround(Ground ground);

    @Delete("delete from ground where groundId=#{groundId}")
    int deleteGround(Integer groundId);

    @Update("update ground set groundName=#{groundName},pricePerHour=#{pricePerHour},address=#{address} " +
            "where groundId=#{groundId}")
    int updateGround(Ground ground);

    @Update("update ground set photo=#{photo} where groundId=#{groundId}")
    int updateGroundPhoto(Ground ground);

    @Select("select photo from ground where groundId=#{groundId}")
    String getImagePathByGroundId(Integer groundId);
}
