package com.example.hng4.Models;

import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true)
    private String name;
}
