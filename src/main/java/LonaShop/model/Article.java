package LonaShop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public Image getAvatarImage() {
        return this.subContentList.get(0).getImage();

    }

}
