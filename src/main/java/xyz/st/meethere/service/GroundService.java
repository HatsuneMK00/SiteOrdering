package xyz.st.meethere.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.st.meethere.entity.Ground;
import xyz.st.meethere.mapper.GroundMapper;

import java.util.List;

@Service
public class GroundService {
    @Autowired
    GroundMapper groundMapper;

    public List<Ground> getAllGrounds() {
        return groundMapper.getAllGrounds();
    }

    public Ground getGroundById(Integer id) {
        return groundMapper.getGroundByGroundId(id);
    }

    public Ground addGround(Ground ground) {
        return groundMapper.addGround(ground);
    }

    public int updateGround(Ground ground) {
        return groundMapper.updateGround(ground);
    }

    public int deleteGround(Integer id) {
        return groundMapper.deleteGround(id);
    }
}

