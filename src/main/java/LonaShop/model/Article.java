package LonaShop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String shortDescription;

    @Column
    private String author;

    @Column
    private Date createdAt;

    @Column
    private Date updatedAt;

    // on = 1 off = 0
    @Column
    private int status = 0;

    // on = 1 off = 0
    @Column
    private int onTop = 0;

    @Column
    private String note;

    @OneToMany(cascade = CascadeType.ALL)
    private List<SubContent> subContentList;

    public Image getAvatar() {
        Image defaultAvatar = null;
        Image avatar = null;
        for (SubContent subContent : subContentList) {
            // check avatar image and return image
            Image image = subContent.getImage();
            if (!ObjectUtils.isEmpty(image)) {
                if (ObjectUtils.isEmpty(defaultAvatar)) {
                    defaultAvatar = image;
                }
                if (image.isAvatar()) {
                    avatar = image;
                    break;
                }
            }
        }

        return ObjectUtils.isEmpty(avatar) ? defaultAvatar : avatar;
    }

}
