package yehor.ua.products.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ProductEntity {
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    private String entryDate;

    private String itemCode;

    private String itemName;

    private String itemQuantity;

    private String status;

    public ProductEntity(String entryDate, String itemCode, String itemName, String itemQuantity, String status) {
        this.entryDate = entryDate;
        this.itemCode = itemCode;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.status = status;
    }
}
