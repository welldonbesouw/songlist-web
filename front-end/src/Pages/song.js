import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { viewService } from "../Services/songService";
import { Button, Card, Container, Spinner } from "react-bootstrap";

const Song = () => {
  const [song, setSong] = useState(null);
  const { songId } = useParams();

  const navigate = useNavigate();

  useEffect(() => {
    viewService(songId).then((response) => setSong(response.data));
  }, []);

  const displayLyrics = (lyrics) => {
    return lyrics.split("\n").map((line, index) => (
      <span key={index}>
        {line}
        <br />
      </span>
    ));
  };

  const goBack = () => {
    navigate(-1);
  };

  return (
    <>
      {song != null ? (
        <Container>
          <Card
            className="mt-4 ms-5 mb-5 song-card"
            border="secondary"
            style={{ width: "18rem" }}
          >
            <Card.Body>
              <Card.Title>{song.title}</Card.Title>
              <Card.Subtitle>Do = {song.key}</Card.Subtitle>
              <br />
              <Card.Text>{displayLyrics(song.lyrics)}</Card.Text>
            </Card.Body>
            <Button variant="secondary" className="my-1 mx-1" onClick={goBack}>
              Back
            </Button>
          </Card>
        </Container>
      ) : (
        <div className="d-flex justify-content-center mt-5">
          <Spinner animation="border" variant="light" />
        </div>
      )}
    </>
  );
};

export default Song;
