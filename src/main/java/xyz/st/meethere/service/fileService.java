package xyz.st.meethere.service;

import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xyz.st.meethere.exception.FileException;

import java.io.File;
import java.nio.file.Path;

@Service
public class fileService {
    private final String uploadPath;

    public fileService() {
        uploadPath = new ApplicationHome().getSource().getParentFile().getPath();
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
            fileName = uploadPath + fileName;
            File dest = new File(fileName);

            file.transferTo(dest);
            return fileName;

        } catch (Exception e) {
            throw new FileException("file upload fail");
        }
    }
}
