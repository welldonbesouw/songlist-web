import React, { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Button, Card, Col, Container, Pagination, Row } from "react-bootstrap";
import { searchService } from "../Services/songService";
import { usePdfRequest } from "../PdfRequestContext";
import { createService } from "../Services/pdfService";
import { usePdfId } from "../PdfIdContext";
import { TiTickOutline } from "react-icons/ti";

const Browse = () => {
  const [songs, setSongs] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [pageSize, setPageSize] = useState(9);

  const { keyword } = useParams();
  const { pdfId } = usePdfId();
  const { pdfRequest, setPdfRequest } = usePdfRequest();

  const navigate = useNavigate();

  useEffect(() => {
    const fetchSong = () => {
      searchService(keyword, currentPage, pageSize)
        .then((response) => setSongs(response.data.content))
        .catch((error) => console.log("Error fetching songs:", error));
    };
    fetchSong();
  }, [keyword, currentPage, pageSize]);

  const truncateLyrics = (lyrics, maxLength) => {
    return lyrics.slice(1, maxLength) + "...";
  };

  const handlePageChange = (page) => {
    setCurrentPage(page - 1);
  };

  const viewSong = (id) => {
    navigate(`/song/${id}`);
  };
  // TODO: Add a notification "Song has been added"
  const addToPdf = (song) => {
    if (pdfId === null || pdfId === undefined) {
      createService().then((response) => {
        const id = response.data.id;
        navigate(`/createPdf/${id}/${keyword}`);
      });
    }

    let addedSongs = [...pdfRequest.selectedSongs];
    let addEmptyComment = [...pdfRequest.comments];

    addedSongs.push(song);
    addEmptyComment.push({ songId: song.id, comment: "" });
    setPdfRequest((prevPdfRequest) => ({
      ...prevPdfRequest,
      selectedSongs: addedSongs,
      comments: addEmptyComment,
    }));
  };

  let isSongAdded = (songId) => {
    const addedSongs = [...pdfRequest.selectedSongs];
    const songIndex = addedSongs.findIndex((item) => item.id === songId);
    if (songIndex !== -1) return true;
    else return false;
  };
  return (
    <>
      <Container>
        <Row>
          {songs.map((song) => (
            <Col lg={4} md={6} className="mt-4">
              <Card
                key={song.id}
                style={{ width: "18rem" }}
                className="song-card"
              >
                <Card.Body>
                  <Card.Title>{song.title}</Card.Title>
                  {song.key != null ? (
                    <Card.Subtitle className="mb-2 text-muted">
                      Do = {song.key}
                    </Card.Subtitle>
                  ) : (
                    <></>
                  )}
                  <Card.Text>{truncateLyrics(song.lyrics, 100)}</Card.Text>
                  <Button className="me-3" onClick={() => viewSong(song.id)}>
                    View
                  </Button>
                  {isSongAdded(song.id) === false ? (
                    <Button onClick={() => addToPdf(song)}>
                      Add to Song List
                    </Button>
                  ) : (
                    <Button variant="secondary" disabled>
                      <TiTickOutline className="tick-style" size={23} /> Added
                    </Button>
                  )}
                </Card.Body>
              </Card>
            </Col>
          ))}
        </Row>
        {songs.length !== 0 ? (
          <div className="mt-5">
            <Pagination>
              <Pagination.First
                onClick={() => handlePageChange(1)}
                disabled={currentPage === 0}
              />
              <Pagination.Prev
                onClick={() => handlePageChange(currentPage)}
                disabled={currentPage === 0}
              />
              <Pagination.Next
                onClick={() => handlePageChange(currentPage + 2)}
                disabled={songs.length < pageSize}
              />
            </Pagination>
          </div>
        ) : (
          <></>
        )}
      </Container>
    </>
  );
};

export default Browse;
