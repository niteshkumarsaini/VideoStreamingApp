package com.videostreaming.VideoStreamingApplication.Entities;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Video {
    @Id
    private String id;
    private String videTitle;
    private String videoDesc;
    private String videoPath;
    private String contentType;
    private String thumbnailType;
    private String thumbnailPath;
    
    @ManyToOne
    private Course course;
    
}
