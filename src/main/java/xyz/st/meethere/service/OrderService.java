package xyz.st.meethere.service;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.st.meethere.entity.PreOrder;
import xyz.st.meethere.mapper.OrderMapper;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

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
        setDate(preOrder);
        return orderMapper.addPreOrder(preOrder);
    }
    public int deletePreOrder(Integer oid){
        return orderMapper.deletePreOrder(oid);
    }
    public List<PreOrder> getGroundOrders(Integer gid){
        List<PreOrder> orders =  orderMapper.getPreOrdersByGroundId(gid);
        return orders;
    }
    public boolean checkGroundExistence(Integer gid){
        return orderMapper.checkGroundExistence(gid) != null;
    }
    public boolean checkUserExistence(Integer uid){
        return orderMapper.checkUserExistence(uid) != null;
    }
    public boolean validatePreOrder(Integer gid, String startTime,Integer duration) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Calendar calendar = Calendar.getInstance();
        Date date = simpleDateFormat.parse(startTime);
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY,duration);
        Date dateAfter = calendar.getTime();
        Date temp = null;
        for(PreOrder preOrder : orderMapper.getPreOrdersByGroundId(gid)){
            temp = simpleDateFormat.parse(preOrder.getStartTime());
            calendar.setTime(temp);
            calendar.add(Calendar.HOUR_OF_DAY,preOrder.getDuration());
            Date tempAfter = calendar.getTime();
            if((date.after(temp)&&date.before(tempAfter))||date.equals(temp))
                return true;
            if((dateAfter.after(temp)&&dateAfter.before(tempAfter))||dateAfter.equals(tempAfter))
                return true;
        }
        return false;
    }
    public List<List> getOrderTime(Integer gid){
        ArrayList<String> orderTimes = new ArrayList<>();
        ArrayList<Integer> durations = new ArrayList<>();
        List<PreOrder> preOrders = orderMapper.getPreOrdersByGroundId(gid);
        Collections.sort(preOrders, new Comparator<PreOrder>() {
            @Override
            public int compare(PreOrder o1, PreOrder o2) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });
        for(PreOrder preOrder : preOrders){
            orderTimes.add(preOrder.getStartTime());
            durations.add(preOrder.getDuration());
        }
        ArrayList<List> lists = new ArrayList<>();
        lists.add(orderTimes);
        lists.add(durations);
        return lists;
    }
    public Integer getGroundPrice(Integer gid){
        return orderMapper.getGroundPrice(gid);
    }
    public void setDate(PreOrder preOrder){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        preOrder.setOrderTime(Timestamp.valueOf(simpleDateFormat.format(new Date())));
    }
}
