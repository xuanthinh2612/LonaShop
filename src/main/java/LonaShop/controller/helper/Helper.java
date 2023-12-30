package LonaShop.controller.helper;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;

@Component
public class Helper {

    public String genRandomFileName(String fileName) {
        return new Timestamp(System.currentTimeMillis()).getTime() + UUID.randomUUID().toString()
                                 + "." + getExtension(fileName);
    }

    private String getExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
}
