package LonaShop.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

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
    private Long quantity; // delete

    @Column
    private int status = 1;

    @Column
    @NotEmpty(message = "Vui lòng điền tên người nhận" )
    private String customerName;

    @Column
    @NotEmpty(message = "Vui lòng điền địa chỉ nhận hàng" )
    private String customerAddress;

    @Column
    @NotEmpty(message = "Vui lòng điền số điện thọai" )
    private String customerPhoneNumber;

    @Column
    private String customerEmail;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Column
    private Long totalAmount;

    @Column
    @NotNull(message = "Vui lòng chọn phương thức thanh toán")
    @Min(value = 1, message = "Phương thức thanh toán không hợp lệ")
    @Max(value = 3, message = "Phương thức thanh toán không hợp lệ")
    private Integer paymentType;

    @Column
    private int paymentStatus;

    @Column
    private String orderCode;

    @Column
    private String userIp;

    @ManyToOne
    private Product product; // delete

    @ManyToOne
    private User user;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<OrderItem> OrderItems;

    @Column
    private Date createdAt;

    @Column
    private Date updatedAt;
}
