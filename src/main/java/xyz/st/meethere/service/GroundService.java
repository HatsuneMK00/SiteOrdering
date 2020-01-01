package xyz.st.meethere.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import xyz.st.meethere.entity.Ground;
import xyz.st.meethere.mapper.GroundMapper;

import java.io.File;
import java.util.List;

@Service
public class GroundService {
    final
    GroundMapper groundMapper;

    public GroundService(GroundMapper groundMapper) {
        this.groundMapper = groundMapper;
    }

    Logger logger = LoggerFactory.getLogger(getClass());

    public List<Ground> getAllGrounds() {
        return groundMapper.getAllGrounds();
    }

    public Ground getGroundById(Integer id) {
        return groundMapper.getGroundByGroundId(id);
    }

    public List<Ground> getGroundsByMatch(String matchParam){
        return groundMapper.getGroundsByGroundNameMatch(matchParam);
    }

    public int addGround(Ground ground) {
        /*
        * 把图片路径封装成前端可以直接使用的形式
        * */
        String filename = ground.getPhoto();

        String[] temp = filename.split("/");
        filename = "/" + temp[temp.length - 2] + "/" + temp[temp.length - 1];
        ground.setPhoto(filename);
        return groundMapper.addGround(ground);
    }

    public int addGroundWOFileOperation(Ground ground) {
        /*
         * 把图片路径封装成前端可以直接使用的形式
         * */
        return groundMapper.addGround(ground);
    }

    public int updateGround(Ground ground) {
        return groundMapper.updateGround(ground);
    }

    public int deleteGround(Integer id) {
        String filename = groundMapper.getImagePathByGroundId(id);
        filename = new ApplicationHome(getClass()).getSource().getParentFile().getPath() +  filename;
        File file = new File(filename);
        if (file.delete()) {
            logger.info(String.format("删除场馆文件成功，场馆id: %d", id));
        } else {
            logger.warn(String.format("删除场馆文件失败，文件名: %s", filename));
        }
        return groundMapper.deleteGround(id);
    }

    public boolean verifyGround(Ground ground) {
        boolean verified = true;
        if (ground.getGroundName() == null || ground.getGroundName().equals(""))
            verified = false;
        if (ground.getAddress() == null || ground.getAddress().equals(""))
            verified = false;
        if (ground.getPricePerHour() <= 0)
            verified = false;
        return !verified;
    }
}

