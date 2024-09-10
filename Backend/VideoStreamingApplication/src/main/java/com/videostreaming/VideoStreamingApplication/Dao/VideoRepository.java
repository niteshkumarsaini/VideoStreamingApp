package com.videostreaming.VideoStreamingApplication.Dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.videostreaming.VideoStreamingApplication.Entities.Video;

@Repository
public interface VideoRepository extends JpaRepository<Video,String>{
    
    

}
