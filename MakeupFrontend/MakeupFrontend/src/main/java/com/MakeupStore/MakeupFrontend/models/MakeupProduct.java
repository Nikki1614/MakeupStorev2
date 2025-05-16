package com.MakeupStore.MakeupFrontend.models;

import lombok.Data;
import java.util.UUID;

@Data
public class MakeupProduct {
    private UUID id;
    private String productName;
    private String category;
    private int MakeupProductQuantity;

    // Getters y setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getProductQuantity() { return MakeupProductQuantity; }
    public void setProductQuantity(int makeupProductQuantity) { this.MakeupProductQuantity = makeupProductQuantity; }
}
