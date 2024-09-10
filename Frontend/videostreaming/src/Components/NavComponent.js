import React from 'react'
import { Button, Navbar } from "flowbite-react";
import { useNavigate } from 'react-router-dom';

const NavComponent = () => {
  const navigate=useNavigate();
  function uploadTab(){
    navigate("/upload")
    
    
  }
  return (
    <Navbar fluid rounded>
    <Navbar.Brand href="/">
      <img src="video.png" className="mr-3 h-6 sm:h-9" alt="Flowbite React Logo" />
      <span className="self-center whitespace-nowrap text-xl font-semibold dark:text-white">Video Streamer</span>
    </Navbar.Brand>
    <div className="flex md:order-2" id="get-start">
      <Button onClick={uploadTab}>Upload Video</Button>
      <Navbar.Toggle />
    </div>
    <Navbar.Collapse>
      <Navbar.Link href="#" active>
        Home
      </Navbar.Link>
      <Navbar.Link href="#">About</Navbar.Link>
      <Navbar.Link href="#">Services</Navbar.Link>
      <Navbar.Link href="#">Pricing</Navbar.Link>
      <Navbar.Link href="#">Contact</Navbar.Link>
    </Navbar.Collapse>
  </Navbar>
  )
}

export default NavComponent

