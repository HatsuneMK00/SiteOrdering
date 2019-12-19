package xyz.st.meethere.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;
import xyz.st.meethere.entity.PreOrder;

import java.util.List;

@Repository
public interface OrderMapper {
    //    用户订单管理
    @Select("select * from preorder where userId=#{id}")
    List<PreOrder> getPreOrdersByUserId(Integer id);

    @Delete("delete from preorder where preOrderId=#{preOrderId}")
    int deletePreOrder(Integer preOrderId);

    @Update("update preorder set orderTime=#{orderTime},startTime=#{startTime},price=#{price},duration=#{duration} " +
            "where preOrderId=#{preOrderId}")
    int updatePreOrder(PreOrder preOrder);

    @Options(useGeneratedKeys = true,keyProperty = "preOrderId")
    @Insert("insert into preorder(groundId,userId,orderTime,price,startTime,duration,payed,checked) values(#{groundId}," +
            "#{userId},#{orderTime},#{price},#{startTime},#{duration},#{payed},#{checked})")
    int addPreOrder(PreOrder preOrder);

    @Select("select * from preorder where groundId=#{id}")
    List<PreOrder> getPreOrdersByGroundId(Integer id);
    @Select("select * from preorder where userId=#{uid} and preOrderId=#{oid}")
    PreOrder getAPreOrderOfUser(Integer uid,Integer oid);

    // 管理员订单管理
//    添加上降序获取功能
    @Select("select * from preorder")
    List<PreOrder> getAllPreOrders();
}
