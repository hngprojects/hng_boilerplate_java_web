package com.example.hng4.Models;



import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Blog {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String author;
    private String title;
    private String content;

    @ElementCollection
    private List<String> tags;

    private LocalDate createdDate;
}

