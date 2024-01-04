package LonaShop.controller.helper;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.UUID;

@Component
public class Helper {

    public String genRandomFileName(String OriginalFilename) {
        return new Timestamp(System.currentTimeMillis()).getTime() + UUID.randomUUID().toString()
                                 + "." + getExtension(OriginalFilename);
    }

    private String getExtension(String OriginalFilename) {
        int dotIndex = OriginalFilename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : OriginalFilename.substring(dotIndex + 1);
    }
}
