package LonaShop.controller.helper;

import LonaShop.common.CommonConst;
import LonaShop.model.Article;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Component
public class Helper {

    public String genRandomFileName(String OriginalFilename) {
        return new Timestamp(System.currentTimeMillis()).getTime() + UUID.randomUUID().toString()
                + "." + getExtension(OriginalFilename);
    }

    public String getRandomOrderCode(String prefixCode) {
        return prefixCode + generateRandomHash(10);
    }

    public String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");  // Lấy từ header proxy
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip.split(",")[0].trim();  // Lấy IP đầu tiên nếu có nhiều proxy
        }

        ip = request.getHeader("X-Real-IP"); // Header khác có thể chứa IP thật
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }

        return request.getRemoteAddr(); // Trả về IP nếu không có proxy
    }



    public Article getMainArticle(List<Article> articleList) {

        for (Article article : articleList) {
            if (article.getOnTop() == CommonConst.FLAG_ON) {
                return article;
            }
        }
        return articleList.get(0);
    }

//   PRIVATE ============================================================================

    private String getExtension(String OriginalFilename) {
        int dotIndex = OriginalFilename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : OriginalFilename.substring(dotIndex + 1);
    }

    private String generateRandomHash(int length) {
        String HEX_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(HEX_CHARS.charAt(random.nextInt(HEX_CHARS.length())));
        }
        return sb.toString();
    }

}
