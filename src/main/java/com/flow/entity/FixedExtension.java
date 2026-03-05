package com.flow.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class FixedExtension {

    @Id
    @Column(length = 20)
    private String name;

    @Column(nullable = false)
    private boolean blocked = false;

    public FixedExtension(String name) {
        this.name = name;
        this.blocked = false;
    }

    public void updateBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
