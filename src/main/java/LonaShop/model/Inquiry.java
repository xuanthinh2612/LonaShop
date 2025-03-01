package LonaShop.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int status = 1;

    @Column
    @NotEmpty(message = "Không được bỏ trống mục này")
    private String customerName;

    @Column
    private String customerAge;

    @Column
    private String customerGender;

    @Column
    private String customerAddress;

    @Column
    @NotEmpty(message = "Không được bỏ trống mục này")
    private String customerPhoneNumber;

    @Column
    private String customerEmail;

    @Column(columnDefinition="TEXT")
    private String content;

    @Column(columnDefinition="TEXT")
    private String note;

    @Column
    private Date createdAt;

    @Column
    private Date updatedAt;

    @ManyToOne
    private User user;
}
