package com.MakeupStore.MakeupFrontend.dto;


import java.util.UUID;
import lombok.Data;

@Data
public class MakeupProductDTO {
    private UUID id;
    private String ProductName;
    private String ProductCategory;
    private Integer ProductQuantity;
}
