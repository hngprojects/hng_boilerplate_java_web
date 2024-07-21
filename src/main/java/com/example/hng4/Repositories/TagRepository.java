package com.example.hng4.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hng4.Models.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Tag findByName(String name);
}
