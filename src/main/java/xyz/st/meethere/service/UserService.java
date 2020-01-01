package xyz.st.meethere.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import xyz.st.meethere.config.MyServerConfig;
import xyz.st.meethere.entity.User;
import xyz.st.meethere.mapper.UserMapper;

import java.io.File;
import java.util.List;

@Service
public class UserService {
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    Logger logger = LoggerFactory.getLogger(getClass());

    public User getUserByName(String userName) {
        return userMapper.getUserByName(userName);
    }

    public User getUserById(int userId) {
        return userMapper.getUserById(userId);
    }

    public List<User> traverseUser() {
        return userMapper.getAllUser();
    }

    public List<User> traverseUserWOAuthority() {
        return userMapper.getAllUserWOAuthority();
    }

    public boolean checkUserPassword(String name, String pwd) {
        User user = userMapper.getUserByName(name);
        return user != null && user.getPassword().equals(pwd);
    }

    public int addUser(String email, String userName, String password) {
        User user = new User();
        user.setEmail(email);
        user.setUserName(userName);
        user.setPassword(password);
        User existedUser = userMapper.getUserByNameWOAuthority(userName);
        if (existedUser != null) return 0;
        return userMapper.addUser(user);
    }

    public int deleteUserById(int userId) {
        /*
         * 删除用户头像
         * */
        String filename = userMapper.getUserById(userId).getProfilePic();
        filename = new ApplicationHome(getClass()).getSource().getParentFile().getPath() + filename;
        File file = new File(filename);
        if (file.delete()) {
            logger.info(String.format("删除用户头像文件成功，用户id: %d", userId));
        } else {
            logger.warn(String.format("删除用户头像文件失败，文件名: %s", filename));
        }

        return userMapper.deleteUserById(userId);
    }

    public int updateUserByModel(User user) {
        User existedUser = userMapper.getUserByNameWOAuthority(user.getUserName());
        if (existedUser != null && existedUser.getUserId() != user.getUserId()) return 0;
        return userMapper.updateUser(user);
    }

    public int updateUserProfilePicByUserId(String profilePic, int userId) {
        String[] temp = profilePic.split("/");

        // Default server regarded as [localhost]

//        使用私有变量的时候没有赋上值
//        改为直接使用MyServerConfig中的static属性
        String profileUrl = MyServerConfig.server + ":" + MyServerConfig.port + "/images/";
        logger.info(MyServerConfig.port);
        profileUrl = profileUrl + temp[temp.length - 1];
        int result = userMapper.updateUserProfilePicByUserId(userId, profileUrl);
        if (result == 1)
            return 200;
        else
            return 500;
    }
}
