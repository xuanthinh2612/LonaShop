package LonaShop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @NotEmpty(message = "Mô tả không thể để trống")
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @NotNull(message = "Hãy chọn một ảnh")
    private Image image;
}
