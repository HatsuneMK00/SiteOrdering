package xyz.st.meethere.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.st.meethere.exception.FileException;

import java.io.File;
import java.nio.file.Path;

@Service
public class FileService {
    private final String uploadPath;

    Logger logger = LoggerFactory.getLogger(getClass());

    public FileService() {
        uploadPath = new ApplicationHome(getClass()).getSource().getParentFile().getPath() + "/images/";
    }

    public String storeFile(MultipartFile file) throws FileException {
        String fileName = file.getOriginalFilename();

        try {
            if (fileName == null) {
                throw new FileException("file not uploaded successfully");
            }
            if (fileName.contains("..")) {
                throw new FileException("file has invalid filename");
            }
            File dir = new File(uploadPath);
            if (!dir.exists()){
                if (dir.mkdir()) {
                    logger.info("创建文件上传目录成功" + uploadPath);
                }else {
                    logger.warn("创建文件上传目录失败" + uploadPath);
                }
            }
            fileName = uploadPath + fileName;
            logger.info("uploadPath: " + uploadPath);
            logger.info("filename: " + fileName);

            File dest = new File(fileName);

            file.transferTo(dest);
            return fileName;

        } catch (Exception e) {
            throw new FileException("file upload fail");
        }
    }
}
