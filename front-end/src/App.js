import "./App.css";
import { Routes, Route } from "react-router-dom";
import Home from "./Pages/home";
import Browse from "./Pages/browse";
import "bootstrap/dist/css/bootstrap.min.css";
import Song from "./Pages/song";
import Header from "./Components/Header";
import CreatePdf from "./Pages/createPdf";
import { SongListProvider } from "./SongListContext";
import Footer from "./Components/Footer";

function App() {
  return (
    <>
      <SongListProvider>
        <Header />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path=":keyword/" element={<Browse />} />
          <Route path="/song/:songId" element={<Song />} />
          <Route path="/createPdf/:pdfId" element={<CreatePdf />}>
            <Route path=":keyword" element={<CreatePdf />} />
          </Route>
        </Routes>
        <Footer />
      </SongListProvider>
    </>
  );
}

export default App;
