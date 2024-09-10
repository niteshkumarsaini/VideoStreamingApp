package com.videostreaming.VideoStreamingApplication.Entities;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Course {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    private String name;
    @OneToMany(mappedBy = "course")
    private List<Video>videos;

    
}
