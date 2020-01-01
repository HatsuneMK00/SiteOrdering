package xyz.st.meethere.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.st.meethere.config.MyServerConfig;
import xyz.st.meethere.entity.User;
import xyz.st.meethere.mapper.AdminMapper;

@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper;

    public User getAdminByName(String userName) {
        return adminMapper.getAdminByName(userName);
    }

    public User getAdminById(int userId) {
        return adminMapper.getAdminById(userId);
    }

    public boolean checkAdminPassword(String name, String pwd) {
        User user = adminMapper.getAdminByName(name);
        return user != null && user.getPassword().equals(pwd);
    }

    public int updateAdminByModel(User user) {
        User existedAdmin = adminMapper.getUserByNameWOAuthority(user.getUserName());
        if (existedAdmin != null && existedAdmin.getUserId() != user.getUserId()) return 0;
        return adminMapper.updateAdmin(user);
    }

    public int updateAdminProfilePicByAdminId(String profilePic, int userId) {
        String[] temp = profilePic.split("/");

        // Default server regarded as [localhost]
        String profileUrl = MyServerConfig.server + ":" + MyServerConfig.port + "/images/";
        profileUrl = profileUrl + temp[temp.length - 1];
        int result = adminMapper.updateAdminProfilePicByAdminId(userId, profileUrl);
        if (result == 1)
            return 200;
        else
            return 500;
    }
}
