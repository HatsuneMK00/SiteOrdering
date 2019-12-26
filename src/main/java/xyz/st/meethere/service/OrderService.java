package xyz.st.meethere.service;

import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(getClass());

    public List<PreOrder> getAllPreOrdersOfUser(Integer id) {
        return orderMapper.getPreOrdersByUserId(id);
    }

    public PreOrder getPreOrder(Integer uid, Integer oid) {
        return orderMapper.getAPreOrderOfUser(uid, oid);
    }

    public int addPreOrder(PreOrder preOrder) {
        setDate(preOrder);
        return orderMapper.addPreOrder(preOrder);
    }

    public int deletePreOrder(Integer oid) {
        return orderMapper.deletePreOrder(oid);
    }

    public List<PreOrder> getGroundOrders(Integer gid) {
        List<PreOrder> orders = orderMapper.getPreOrdersByGroundId(gid);
        return orders;
    }

    public boolean checkGroundExistence(Integer gid) {
        return orderMapper.checkGroundExistence(gid) != null;
    }

    public boolean checkUserExistence(Integer uid) {
        return orderMapper.checkUserExistence(uid) != null;
    }

    public boolean checkPreOrderExistence(Integer pid) {
        return orderMapper.getPreOrderByPid(pid) != null;
    }

    public boolean validatePreOrder(Integer gid, String startTime, Integer duration){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = simpleDateFormat.parse(startTime);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        calendar.setTime(date);
        calendar.add(Calendar.HOUR_OF_DAY, duration);
        Date dateAfter = calendar.getTime();
        Date temp = null;
        for (PreOrder preOrder : orderMapper.getPreOrdersByGroundId(gid)) {
            try {
                temp = simpleDateFormat.parse(preOrder.getStartTime());
            } catch (ParseException e) {
                logger.error(e.getMessage(), e);
            }
            calendar.setTime(temp);
            calendar.add(Calendar.HOUR_OF_DAY, preOrder.getDuration());
            Date tempAfter = calendar.getTime();
            if ((date.after(temp) && date.before(tempAfter)) || date.equals(temp))
                return true;
            if ((dateAfter.after(temp) && dateAfter.before(tempAfter)) || dateAfter.equals(tempAfter))
                return true;
        }
        return false;
    }

    public List<List> getOrderTime(Integer gid) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.0");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Calendar calendar = Calendar.getInstance();
        Date date = null;
        try {
            date = simpleDateFormat.parse(simpleDateFormat.format(new Date()));
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        ArrayList<String> orderTimes = new ArrayList<>();
        ArrayList<Integer> durations = new ArrayList<>();
        List<PreOrder> preOrders = orderMapper.getPreOrdersByGroundId(gid);
        Collections.sort(preOrders, new Comparator<PreOrder>() {
            @Override
            public int compare(PreOrder o1, PreOrder o2) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });
        boolean flag = false;
        for (PreOrder preOrder : preOrders) {
            if (!flag) {
                Date temp = null;
                try {
                    temp = simpleDateFormat.parse(preOrder.getStartTime());
                } catch (ParseException e) {
                    logger.error(e.getMessage(), e);
                }
                calendar.setTime(temp);
                calendar.add(Calendar.HOUR_OF_DAY, preOrder.getDuration());
                Date tempAfter = calendar.getTime();
                if ((date.after(temp) && date.before(tempAfter)) || date.equals(temp) || date.before(temp))
                    flag = true;
            }
            if (flag) {
                orderTimes.add(preOrder.getStartTime());
                durations.add(preOrder.getDuration());
            }
        }
        ArrayList<List> lists = new ArrayList<>();
        lists.add(orderTimes);
        lists.add(durations);
        return lists;
    }

    public List<PreOrder> getUncheckedOrders() {
        return orderMapper.getAllUncheckedOrders();
    }

    public int checkPreOrder(Integer id) {
        return orderMapper.checkPreOrder(id);
    }

    public int checkPreOrderFail(Integer id) {
        return orderMapper.checkPreOrderFail(id);
    }

    public Integer getGroundPrice(Integer gid) {
        return orderMapper.getGroundPrice(gid);
    }

    public void setDate(PreOrder preOrder) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        preOrder.setOrderTime(Timestamp.valueOf(simpleDateFormat.format(new Date())));
    }

    public List<PreOrder> getOrders() {
        return orderMapper.getAllPreOrders();
    }
}
