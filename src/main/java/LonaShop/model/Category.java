package LonaShop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import java.util.List;

@Table
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @Column
    @NotEmpty(message = "Tên không thể để trống")
    private String name;

    @Column
    private String description;

    @OneToMany(cascade = CascadeType.ALL)
    @Nullable
    private List<Product> productList;

    @OneToOne(cascade = CascadeType.ALL)
    @Nullable
    private Image image;
}
