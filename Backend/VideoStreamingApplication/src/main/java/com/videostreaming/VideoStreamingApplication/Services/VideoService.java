package com.videostreaming.VideoStreamingApplication.Services;

import java.util.List;
import java.util.UUID;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.videostreaming.VideoStreamingApplication.Entities.Video;


public interface VideoService {

    Video save(MultipartFile file,MultipartFile thumbnail,Video video);
    Video get(String id);
    Video processVideo(String videoId);
    List<Video>getAllVideos();

    

    
}
