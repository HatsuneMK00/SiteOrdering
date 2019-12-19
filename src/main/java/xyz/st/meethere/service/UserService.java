package xyz.st.meethere.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.st.meethere.entity.User;
import xyz.st.meethere.mapper.UserMapper;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public User getUserByName(String userName){
        User user = userMapper.getUserByName(userName);
        return user;
    }

    public User getUserById(int userId){
        User user = userMapper.getUserById(userId);
        return user;
    }

    public List<User> traverseUser(){
        return userMapper.getAllUser();
    }

    public boolean checkUserPassword(String name, String pwd){
        User user = userMapper.getUserByName(name);
        if(user!=null && user.getPassword().equals(pwd))
            return true;
        return false;
    }

    public int addUser(String email, String userName, String password){
        User user = new User();
        user.setEmail(email);user.setUserName(userName);user.setPassword(password);
        User existed_user = userMapper.getUserByNameWOAuthority(userName);
        if(existed_user!=null) return 0;
        return userMapper.addUser(user);
    }

    public int deleteUserById(int userId){
        return userMapper.deleteUserById(userId);
    }

    public int updateUserByModel(User user){
        User existed_user = userMapper.getUserByNameWOAuthority(user.getUserName());
        if(existed_user!=null&&existed_user.getUserId()!=user.getUserId()) return 0;
        return  userMapper.updateUser(user);
    }


}
