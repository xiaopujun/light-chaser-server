package com.dagu.lightchaser.global;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GlobalVariables {
    public static final String PROJECT_PATH = System.getProperty("user.dir").replaceAll("\\\\", "/");

    public static final String[] IMAGE_TYPE = new String[]{".bmp", ".jpg", ".jpeg", ".png", ".gif"};

    public static final long IMAGE_SIZE = 1024 * 1024 * 5;

    public static String IMAGE_PATH;

    @Value("${light-chaser.image.path}")
    public void setImagePath(String path) {
        IMAGE_PATH = path;
    }

}
