package LonaShop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

@Getter
@Setter
@Table
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column
    private String imageName;

    @Column
    private String ImageUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description_1;

    @Column(columnDefinition = "TEXT")
    private String description_2;

    @Column(columnDefinition = "TEXT")
    private String description_3;

    @Column
    private String note;

    @Nullable
    @ManyToOne
    private Product product;
}
