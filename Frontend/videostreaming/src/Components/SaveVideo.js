import React, { useState,useRef,useEffect } from 'react'
import { Card, Textarea } from "flowbite-react";
import { FileInput, Label } from "flowbite-react";
import { Button } from "flowbite-react";
import { TextInput } from 'flowbite-react';
import axios from 'axios';
import { Alert } from "flowbite-react";
import { Progress } from "flowbite-react";
import { ToastContainer } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import NavComponent from './NavComponent';

export const SaveVideo = () => {
  useEffect(()=>{
    document.title="Upload Video"
  })

const [meta,setMeta]=useState({
  title:"",
  desc:""
});

const[video,setVideo]=useState(null)
const[uploadProgress,setUploadProgress]=useState(0);
const[progress,setProgress]=useState(false);
const fileInputRef = useRef(null);
const thumbnailRef=useRef(null);
const[message,setMessage]=useState("")
const[messageType,setMessageType]=useState("")
const [thumbnail,setThumbnail]=useState(null)

const fieldChange=(event)=>{  
setMeta({...meta,[event.target.name]:event.target.value})
}
const fileChange=(event)=>{
  setVideo(event.target.files[0])

}
const thumbnailChange=(event)=>{
  setThumbnail(event.target.files[0]);
  // console.log(thumbnail)

}

const baseUrl="http://localhost:8080/api/v1/videos";
async function sendRequest(){
  try{
  let formData=new FormData();
  formData.append("title",meta.title);
  formData.append("desc",meta.desc)
  formData.append("file",video)
  formData.append("thumbnail",thumbnail);
  const response=await axios.post(`${baseUrl}/save`,formData,{
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    onUploadProgress:function(progressEvent){
      setProgress(true)
      const percentCompleted = Math.round((progressEvent.loaded * 100) / progressEvent.total);
  
    setUploadProgress(percentCompleted)
    }
  })
console.log(response.data);
setMessageType("green")
setMessage("Video has been uploaded Successfully.")

setMeta({
  "title":"",
  "desc":""
})
fileInputRef.current.value = '';
setVideo(null)
setProgress(false)
  }
  catch(error){
    console.log(error);
    setMessage("Video uploading failed..")
  }
}

function clickFiled(){
  document.getElementById("file-upload").click();
}



const formSubmit=(event)=>{
  event.preventDefault();
  console.log(meta,video,thumbnail)
  sendRequest();
 
 


}
  return (
    <>
    <NavComponent/>
    <div className=''>
    <div className='dark:text-white pt-7 flex container text-center justify-center'>
        <h1 className="text-2xl font-bold ">Video Streaming App</h1>

    </div>
    <div className='container flex mt-5  justify-center'>
       
    <Card href="#" className="max-w-xl" >
      <form onSubmit={formSubmit}>
    <div>
    <div className='container'>
            <TextInput className='text-center' placeholder='Enter Video Title' value={meta.title}  onChange={fieldChange} name="title"></TextInput>
        </div>
        <div className='container mt-3'>
            <Textarea className='' placeholder='Enter Video Description' rows={5} value={meta.desc} onChange={fieldChange} name='desc'  style={{resize:"none"}}></Textarea>
        </div>
      <div className="mb-2 block mt-3">
        <Label htmlFor="file-upload" value="Upload Video File" />
      </div>
      <div className='flex justify-center ' style={{alignItems:"center"}}>
      <img alt="" src="play-button.png" className="mr-3 h-14" style={{filter:"invert(2)"}} onClick={clickFiled}/>
      <FileInput id="file-upload" type="file" name="file" onChange={fileChange}  ref={fileInputRef} />
      
      </div>
      <div className="mb-2 block mt-3">
        <Label htmlFor="file-upload" value="Upload Thumbnail" />
      </div>
      <div className='flex justify-center my-3 ' style={{alignItems:"center"}}>
      <img alt="" src="file-image.png" className="mr-3 h-11" style={{filter:"invert(2)"}} onClick={clickFiled}/>
      <FileInput id="file-upload" type="file" name="thumbnail" onChange={thumbnailChange}  ref={thumbnailRef} />
      
      </div>
     {
      message!==""?<>
       <div className='my-3'>
      <Alert color="green">
      <span className="font-medium">{message}</span>
    </Alert>

      </div>
      
      </>:<></>
   
     }
       {
      progress?<>
      <div className='mt-3 mb-4'>
      <Progress
      progress={uploadProgress}
      progressLabelPosition="inside"
      textLabel="Uploading.."
      textLabelPosition="outside"
      size="lg"
      labelProgress
      labelText
    />
    </div>
      
      </>:<></>
   
     }





     
    </div>
     <div className='container flex justify-center mt-3'>
     <Button color="pink" type='submit' >
       
        Upload
      </Button>
     </div>
     </form>

    </Card>
    </div>
    <ToastContainer />
    </div>
    </>
  )
}
