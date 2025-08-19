package com.app.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "properties")
@NoArgsConstructor
@Getter
@Setter
@ToString(callSuper = true, exclude = {"images", "blockoutDates", "bookings", "propertyAmenities"})
public class Property extends BaseEntity {

    @Column(name = "property_name", length = 100, nullable = false)
    private String propertyName;

    @Column(length = 200)
    private String description;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private boolean availablity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "property_type")
    private PropertyType propertyType;

    @Column(name = "status", nullable = false)
    private boolean status = true;

    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PropertyImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<PropertyBlockout> blockoutDates = new ArrayList<>();

    @OneToMany(mappedBy = "property", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PropertyAmenities> propertyAmenities = new ArrayList<>();

    @OneToOne(mappedBy = "property", cascade = CascadeType.ALL, orphanRemoval = true)
    private PropertyAddress propertyAddress;

    public Property(String propertyName, String description, double price, boolean availablity,
                    PropertyType propertyType, PropertyAddress propertyAddress, boolean status, User owner) {
        super();
        this.propertyName = propertyName;
        this.description = description;
        this.price = price;
        this.availablity = availablity;
        this.propertyType = propertyType;
        this.propertyAddress = propertyAddress;
        this.status = status;
        this.owner = owner;
    }
}