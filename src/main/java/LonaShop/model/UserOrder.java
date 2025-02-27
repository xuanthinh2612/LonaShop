package LonaShop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long quantity;

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
    private String note;

    @Column
    private Date createdAt;

    @Column
    private Date updatedAt;

    @Column
    private Long totalAmount;

    @Column
    private int paymentType;

    @Column
    private int paymentStatus;

    @Column
    private String orderCode;

    @Column
    private String userIp;

    @ManyToOne
    private Product product;

    @ManyToOne
    private User user;
}
