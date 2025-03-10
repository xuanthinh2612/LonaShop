package LonaShop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long quantity;

    @Column
    private Long priceAtTime;

    @Column
    private Long subAmount;

    @ManyToOne
    private Product product;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private UserOrder order;

    @Column
    private Date createdAt;

    @Column
    private Date updatedAt;

}
