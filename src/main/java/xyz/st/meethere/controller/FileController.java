package xyz.st.meethere.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.st.meethere.config.MyServerConfig;
import xyz.st.meethere.entity.ResponseMsg;
import xyz.st.meethere.exception.FileException;
import xyz.st.meethere.service.FileService;

@RestController
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }


    /*
     * 个人信息头像管理
     * */
    @ResponseBody
    @ApiOperation("上传图片文件,返回url")
    @PostMapping("/file/uploadImage")
    ResponseMsg updateProfilePic(@RequestParam("image")MultipartFile file){
        /*
         * 封装图片路径
         * */
        ResponseMsg responseMsg = new ResponseMsg();
        responseMsg.setStatus(404);
        String storeFile = null;
        try {
            storeFile = fileService.storeFile(file);
        } catch (FileException e) {
            e.printStackTrace();
            return responseMsg;
        }
        if(storeFile == null)
            return responseMsg;
        responseMsg.setStatus(200);

        String[] temp = storeFile.split("/");
        String server = MyServerConfig.server;
        String port   = MyServerConfig.port;
        // Default server regarded as [localhost]
        String profile_url="http://"+server+":"+port+ "/images/";
        profile_url = profile_url + temp[temp.length - 1];
        responseMsg.getResponseMap().put("url",profile_url);

        return responseMsg;
    }
}