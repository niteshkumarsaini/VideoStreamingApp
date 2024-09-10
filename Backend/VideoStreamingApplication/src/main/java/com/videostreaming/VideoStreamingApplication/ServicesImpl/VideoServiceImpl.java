package com.videostreaming.VideoStreamingApplication.ServicesImpl;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import com.videostreaming.VideoStreamingApplication.Dao.VideoRepository;
import com.videostreaming.VideoStreamingApplication.Entities.Video;
import com.videostreaming.VideoStreamingApplication.Services.VideoService;

import ch.qos.logback.core.util.StringUtil;
import jakarta.annotation.PostConstruct;


@Service
public class VideoServiceImpl implements VideoService {
    private static final Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);

    @Value("${file.video}")
    String DIR;
    
    @Value("${files.hls}")
    String HLS_DIR;

    @Value("${thumbnail.dir}")
    String THUMBNAIL_DIR;

    @Autowired
    private VideoRepository videoRepository;


    @PostConstruct
    public void init(){
        try{
        File directory=new File(DIR);
        if(directory.exists()){
            logger.info("Video Directory is already exist.");

        }
        else{
            directory.mkdir();
            logger.info("Video Directory has been created successfully.");
        }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Video save(MultipartFile file,MultipartFile thumbnail,Video video) {
        logger.info("save video method is running.");
        try{
        String originalFilename=file.getOriginalFilename();
        String contentType=file.getContentType();
        if(contentType.equals("video/mp4")){

        }
    
        //Saving Thumbnail
        Path thumbnailPath=Paths.get(StringUtils.cleanPath(THUMBNAIL_DIR),video.getId(),thumbnail.getOriginalFilename());
        File thumbnailDir=new File(THUMBNAIL_DIR+"/"+video.getId());
        if(thumbnailDir.exists()){
            System.out.println("Thumbnail Directory already exist");
        }
        else{
            thumbnailDir.mkdirs();
            System.out.println("Thumbnail Directory created..");
        
        }
        
        video.setThumbnailType(thumbnail.getContentType());
        video.setThumbnailPath(thumbnailPath.toString());
        InputStream thumbnailInputStream=thumbnail.getInputStream();
        Files.copy(thumbnailInputStream, thumbnailPath, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("Thumbnail Uploaded Successfully..");




        video.setContentType(contentType);
       InputStream inputStream= file.getInputStream();
       System.out.println(originalFilename);
       String cleanFilename=StringUtils.cleanPath(originalFilename);
       String cleanDir=StringUtils.cleanPath(DIR);
       Path filePath=Path.of(cleanDir, cleanFilename);
       video.setVideoPath(filePath.toString());
       Files.copy(inputStream, filePath,StandardCopyOption.REPLACE_EXISTING);
       videoRepository.save(video);
    //    processVideo(video.getId());
       
       logger.info("File Saved Successfully..");

    
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return video;
    }

    @Override
    public Video get(String id) {
        Video video=videoRepository.findById(id).get();
        return video;
        
    }
@Override
public Video processVideo(String videoId) {
    // Fetching Video from the repo
    Video video = videoRepository.findById(videoId).orElse(null);
    if (video == null) {
        logger.error("Video not found with id: " + videoId);
        return null;
    }

    // Making Directory for the HLS files
    try {
        File dir = new File(HLS_DIR);
        if (dir.exists() || dir.mkdirs()) {
            logger.info("Directory is created or already exists.");
        } else {
            logger.error("Failed to create directory.");
            return null;
        }

        // Fetching file path
        String filepath = video.getVideoPath();
        Path videoPath = Paths.get(filepath);

        Path outputPath = Paths.get(HLS_DIR, videoId);
        Files.createDirectories(outputPath);

        logger.info("All Directories related to different segments have been created.");

        // Construct FFmpeg command
        String ffmpegCmd = String.format(
            "ffmpeg -i \"%s\" -c:v libx264 -c:a aac -strict -2 -f hls -hls_time 10 -hls_list_size 0 -hls_segment_filename \"%s/segment_%%03d.ts\" \"%s/master.m3u8\"",
            videoPath.toAbsolutePath(), outputPath.toAbsolutePath(), outputPath.toAbsolutePath()
        );

        logger.info("Executing command: " + ffmpegCmd);

        // Run FFmpeg command
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", ffmpegCmd);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logger.info(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new Exception("Video Processing failed with exit code: " + exitCode);
        }
        
        logger.info("Video Processed Successfully.");
        File f=new File(videoPath.toString());
        f.delete();
        logger.info("Actual Video Deleted Successfully..");
    } catch (Exception e) {
        logger.error("Video Processing failed.", e);
    }

    return null;
}

@Override
public List<Video> getAllVideos() {
   List<Video>videos=videoRepository.findAll();
   return videos;
   
}





    
}
