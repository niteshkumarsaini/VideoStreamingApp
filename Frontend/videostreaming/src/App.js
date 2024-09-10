
import './App.css';
import { SaveVideo } from './Components/SaveVideo';
import NavComponent from './Components/NavComponent';
import { VideoPlayer } from './Components/VideoPlayer';
import { BrowserRouter as Router, Routes,Route } from 'react-router-dom';


function App() {
  return (
    <div className="App" >
<Router basename='/' >
<Routes>
  <Route path='/' element={<VideoPlayer/>}></Route>

      {/* <SaveVideo/> */}
      {/* <VideoPlayer/> */}
      <Route path='/upload' element={<SaveVideo/>}/>
      </Routes>
      </Router>
    </div>
  );
}

export default App;
