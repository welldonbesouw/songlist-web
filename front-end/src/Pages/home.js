import React from "react";
import { SiApplemusic } from "react-icons/si";
import { Button, Container } from "react-bootstrap";
import Browse from "./browse";
import { useNavigate } from "react-router-dom";
import { createService } from "../Services/pdfService";

const Home = () => {
  const navigate = useNavigate();

  const createPdf = () => {
    createService().then((response) => {
      const id = response.data.id;
      navigate(`/createPdf/${id}`);
    });
  };

  return (
    <>
      <Container>
        <Browse />
        <div className="mt-5">
          <h1 className="create-title color1">CREATE</h1>
          <h1 className="create-title color2">YOUR OWN</h1>
          <h1 className="create-title color3">SONG LIST</h1>
          <div className="create-button-container">
            <Button
              className="create-button fs-5"
              variant="primary"
              onClick={() => createPdf()}
            >
              <span className="create-bold">Create PDF</span> &nbsp;
              <SiApplemusic />
            </Button>
          </div>
        </div>
      </Container>
    </>
  );
};

export default Home;
