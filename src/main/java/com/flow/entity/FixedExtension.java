package com.flow.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
public class FixedExtension {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String name;

    @Column(nullable = false)
    private boolean blocked = false;

    public FixedExtension(String name) {
        this.name = name;
        this.blocked = false;
    }
}
