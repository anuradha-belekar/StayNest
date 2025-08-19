package com.app.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "property_amenities")
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = {"property", "amenity"})
public class PropertyAmenities extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    private Property property;

    @ManyToOne
    @JoinColumn(name = "amenity_id", nullable = false)
    private Amenities amenity;

    @Column(name = "status", nullable = false)
    private boolean status = true; // For soft delete

    public PropertyAmenities(Property property, Amenities amenity) {
        this.property = property;
        this.amenity = amenity;
    }
}