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
@Table(name = "cart_item")
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long quantity;

    @Column
    private Long price_at_time;

    @Column
    private Long subAmount;

    @ManyToOne
    private Cart cart;

    @ManyToOne
    private Product product;

    @Column
    private Date createdAt;

    @Column
    private Date updatedAt;

}
