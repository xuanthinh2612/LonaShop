package LonaShop.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
@Entity
public class SubContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column
    private String title;

    @Column
    private String subTitle;

    @Column(columnDefinition = "TEXT")
    private String content1;

    @Column(columnDefinition = "TEXT")
    private String content2;

    @Column(columnDefinition = "TEXT")
    private String content3;

    @Column
    private String note;

    @ManyToOne
    @Nullable
    private Product product;

    @ManyToOne
    @Nullable
    private Article article;

    @OneToOne(cascade = CascadeType.ALL)
    @Nullable
    private Image image;

}
