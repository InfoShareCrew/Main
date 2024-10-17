package com.infoShare.calog.global.Util;

import com.infoShare.calog.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UtilService {
    private final UserService userService;

    @Value("${custom.fileDirPath}")
    private String fileDirPath;

    public String saveImage(String folder, MultipartFile profileImg) {
        String imageNailrelPath = folder + "/" + UUID.randomUUID().toString() + ".jpg";
        File imageFile = new File(fileDirPath + "/" + imageNailrelPath);

        try {
            profileImg.transferTo(imageFile);
            return imageNailrelPath;
        } catch (IOException e) {
            throw new RuntimeException("img upload error");
        }
    }
}
