package LonaShop.model;

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
    private String customerName;

    @Column
    private String customerAge;

    @Column
    private String customerGender;

    @Column
    private String customerAddress;

    @Column
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
