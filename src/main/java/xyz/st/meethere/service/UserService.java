package xyz.st.meethere.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.st.meethere.entity.User;
import xyz.st.meethere.mapper.UserMapper;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User getUserByName(String name){
        User user = userMapper.getUserByName(name);
        return user;
    }
}
