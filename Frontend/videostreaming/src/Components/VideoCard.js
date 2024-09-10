import React from 'react'
import { Card } from "flowbite-react";


export const VideoCard = ({videoTitle,videoId,changeVideo}) => {
  return (
  <>
<div className='flex' style={{display:"flex",flexDirection:"column",justifyContent:"center",alignItems:"center",border:"1px solid white",borderRadius:"30px",width:"200px",cursor:"pointer",marginTop:"15px"}} onClick={changeVideo}>
    
    <img src={`http://localhost:8080/api/v1/videos/thumbnail/${videoId}`} style={{width:"200px",borderRadius:"30px",borderBottomLeftRadius:"0",borderBottomRightRadius:"0",border:"none"}}/>
    <h6 className='text-center' style={{color:"white",fontSize:"12px",padding:"2px 20px"}}>{videoTitle}</h6>
</div>
  </>
  )
}
