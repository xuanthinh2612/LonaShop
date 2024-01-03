package LonaShop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Cover {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotEmpty(message = "Mục này không thể để trống")
    private String title;

    @Column
    @NotEmpty(message = "Mục này không thể để trống")
    private String description;

    @Column
    @NotEmpty(message = "Mục này không thể để trống")
    private String targetLink;

    @Column
    private String note;

    // MAIN_COVER = 2 SUB_COVER = 1; DISABLED = 0;
    @Column
    private int status = 0;

    @OneToOne(cascade = CascadeType.ALL)
    private Image image;
}
