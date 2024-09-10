import React, { useEffect, useState,useRef } from 'react'
import NavComponent from './NavComponent'
import { VideoCard } from './VideoCard'
import axios from 'axios'
import { Button } from 'flowbite-react'



export const VideoPlayer = () => {
  const [videos,setVideos]=useState(null);
  const [videoId,setVideoId]=useState(null)
  const [vTitle,setVTitle]=useState("");
  const [isPlaying, setIsPlaying] = useState(true);
  const player=useRef(null);
  console.log(player.current)
  async function getAllVideos(){
    const response=await axios.get("http://localhost:8080/api/v1/videos/listvideos");
    setVideos(response.data);
   if(response.data.length!==0){
  setVideoId(response.data[0].id)
  setVTitle(response.data[0].videTitle)
   }
           }



    useEffect(()=>{
        document.title="Stream"
   getAllVideos();





    },[])

function changeVideo(id,title){
  console.log("Called me",id)
  setVideoId(id);
  setVTitle(title)
  console.log(title)
}
const handlePlayPause = () => {
  const video = player.current;
  if (isPlaying) {
    video.pause();  // Pause the video
  } else {
    video.play();  // Play the video
  }
  setIsPlaying(!isPlaying);  // Toggle the play/pause state
};



    
  return (
    <>
    <NavComponent/>
{
  videoId==null?<>
  <div style={{width:"100vw",display:"flex",justifyContent:"center",height:"35vh",alignItems:"center",flexDirection:"column"}} className=''>
  <h1 style={{color:"white",fontSize:"24px"}}>Sorry, No Videos to Display.</h1>
  <img src="facebook.png" className='my-2' style={{height:"80px",width:"80px"}}/>
  </div>
  
  
  </>:<>
  
  <div className='flex my-8' style={{display:"flex", justifyContent:"space-between",height:"70vh"}}>
<div className='flex ' style={{width:"60vw",justifyContent:"center",alignItems:"center",flexDirection:"column"}} >
<video
    id="my-video"
    class="video-js"
    controls={false}
    preload="auto"
    width="700"
    height="264"
    poster={`http://localhost:8080/api/v1/videos/thumbnail/${videoId}`}  
    data-setup="{}"
    src={`http://localhost:8080/api/v1/videos/stream/${videoId}`} ref={player} autoPlay={isPlaying}
  >
   
  </video>
  <div className='container '>
    <h2 style={{color:"white",fontSize:"20px",fontWeight:"bold"}} className='my-2 text-center'>{vTitle}</h2>
  </div>
  <div className='container flex mt-3' style={{justifyContent:"center"}}>
  <Button color="blue" onClick={handlePlayPause}> 
  {isPlaying?<><i className='fa-solid fa-pause my-1 mr-1'></i></>:<><i className='fa-solid fa-play my-1 mr-1'></i></>}
  {isPlaying ?'Pause' : 'Play'}
  </Button>

  </div>


</div>
<div className='' style={{width:"20vw",display:"flex",flexDirection:"column",alignItems:"center",paddingTop:"10px",overflowY:"auto"}}>
{
  videos!=null?<>
  {
    videos.map((data)=>{
      return (
        <>
        <VideoCard videoTitle={data.videTitle} videoId={data.id} changeVideo={()=>{changeVideo(data.id,data.videTitle)}}/>
        </>
      )
    })
  }
  
  </>:<></>
}


</div>
</div>
  
  
  
  </>
}


    </>
  )
}
