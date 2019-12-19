package xyz.st.meethere.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.st.meethere.entity.PreOrder;
import xyz.st.meethere.mapper.OrderMapper;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderMapper orderMapper;
    public List<PreOrder> getAllPreOrdersOfUser(Integer id){
        return orderMapper.getPreOrdersByUserId(id);
    }
    public PreOrder getPreOrder(Integer uid,Integer oid){
        return orderMapper.getAPreOrderOfUser(uid,oid);
    }

    public int addPreOrder(PreOrder preOrder){
        return orderMapper.addPreOrder(preOrder);
    }
}
