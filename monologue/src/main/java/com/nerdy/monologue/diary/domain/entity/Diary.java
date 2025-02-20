package com.nerdy.monologue.diary.domain.entity;

import java.util.List;
import jakarta.persistence.*;


@Entity
@Table(name = "diary")
public class Diary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany
    private List<DiaryImage> images;
}
