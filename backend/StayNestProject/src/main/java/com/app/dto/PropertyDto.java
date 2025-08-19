package com.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true)
public class PropertyDto extends BaseDto {
    @NotBlank
    private String propertyName;

    private String description;

    @Positive
    private double price;

    private boolean availablity;

    @NotBlank
    private String propertyType;

    private boolean status = true;

    @NotNull
    private PropertyAddressDto propertyAddress;

    private List<Long> amenityIds = new ArrayList<>();

    public PropertyDto(@NotBlank String propertyName, String description, @Positive double price, boolean availablity,
                       @NotBlank String propertyType, @NotNull PropertyAddressDto propertyAddress, List<Long> amenityIds) {
        super();
        this.propertyName = propertyName;
        this.description = description;
        this.price = price;
        this.availablity = availablity;
        this.propertyType = propertyType;
        this.propertyAddress = propertyAddress;
        this.amenityIds = amenityIds != null ? amenityIds : new ArrayList<>();
    }
}