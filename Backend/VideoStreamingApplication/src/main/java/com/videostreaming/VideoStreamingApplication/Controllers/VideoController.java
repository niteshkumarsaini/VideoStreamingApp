package com.videostreaming.VideoStreamingApplication.Controllers;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.videostreaming.VideoStreamingApplication.Entities.Video;
import com.videostreaming.VideoStreamingApplication.Helper.Message;
import com.videostreaming.VideoStreamingApplication.Services.VideoService;
import com.videostreaming.VideoStreamingApplication.Helper.Constants;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/v1/videos")
public class VideoController {
    private VideoService videoService;

    VideoController(VideoService videoService) {
        this.videoService = videoService;
    }


    @Value("${files.hls}")
    private String HLS_DIR;
    @Value("${thumbnail.dir")
    private String THUMBNAIL_DIR;


    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);
    @PostMapping(value = "/save", produces = "application/json")
    public ResponseEntity<?> save(@RequestParam("file") MultipartFile file, @RequestParam("title") String title,
            @RequestParam("desc") String desc,@RequestParam("thumbnail") MultipartFile thumbnail) {
                System.out.println("Thumbnail");
                System.out.println(thumbnail.getOriginalFilename());
        Video video = new Video();
        video.setId(UUID.randomUUID().toString());
        video.setVideTitle(title);
        video.setVideoDesc(desc);
        if (videoService.save(file, thumbnail,video) != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(video);
        } 
        ;
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Message.builder().message("Something went wrong").type("error").build());
    }

    @GetMapping("/stream/{videoId}")
    public ResponseEntity<Resource> getVideo(@RequestHeader(value = "range", required = false) String range,
            @PathVariable("videoId") String videoId) {
                try{
        System.out.println("Request aagayi hai");
        Video video = videoService.get(videoId);
        String filePath = video.getVideoPath();
        Resource resource = new FileSystemResource(filePath);
        Path path = Paths.get(filePath);
        long fileLength = path.toFile().length();
        if (range == null) {
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(video.getContentType())).body(resource);

        }
        String ranges[] = range.replace("bytes=", "").split("-");
        long start;
        long end;
        start = Long.parseLong(ranges[0]);
        end=start+Constants.CHUNK_SIZE-1;
        if(end>fileLength){
            end=fileLength-1;
        }
            long contentLength = end - start + 1;
        InputStream inputStream = Files.newInputStream(path);
        inputStream.skip(start);
        byte[]data=new byte[(int)contentLength];
        int read=inputStream.read(data,0,data.length);
        System.out.println("Total Bytes "+fileLength);
        System.out.println("Skips Bytes "+(fileLength-start));
        System.out.println("Read Bytes "+read);
     
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("X-Content-Type-Options", "nosniff");
        headers.setContentLength(contentLength);
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).headers(headers).contentType(MediaType.parseMediaType(video.getContentType())).body(new ByteArrayResource(data));
    }
    catch(Exception e){
        e.printStackTrace();
    }
      
return ResponseEntity.badRequest().build();
    }

    @GetMapping("{videoId}/master.m3u8")
    public ResponseEntity<?>serverMaster(@PathVariable("videoId") String videoId){
        Video video=videoService.get(videoId);
        System.out.println("Request Received for m3u8 master");
        System.out.println(video);
        if(video==null){

        }
        
        Path path=Paths.get(HLS_DIR,videoId,"master.m3u8");
        System.out.println(path.toString());
        if(!Files.exists(path)){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        Resource resource=new FileSystemResource(path);
        
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE,"application/vnd.apple.mpegurl").
        body(resource);

    }
    @GetMapping("/listvideos")
    public ResponseEntity<?>listAllVideos(){
        List<Video>videos=videoService.getAllVideos();
        if(videos==null){
            ResponseEntity.status(HttpStatus.NOT_FOUND);
        }
        
        return ResponseEntity.ok().body(videos);
    }   
    @GetMapping("/thumbnail/{videoId}")
    public ResponseEntity<?>getThumbnail(@PathVariable("videoId") String videoId){
       try{
        Video video=videoService.get(videoId);
        if(video==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        }
        Path path=Paths.get(video.getThumbnailPath());
        Resource resource=new UrlResource(path.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +"thumbnail" + "\"")
                .body(resource);
    }
    catch(Exception e){
        e.printStackTrace();
    }
        return null;

    }



}
