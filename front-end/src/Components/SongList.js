import React, { useState } from "react";
import { DragDropContext, Draggable, Droppable } from "react-beautiful-dnd";
import { Button, Form, ListGroup, Modal } from "react-bootstrap";
import { usePdfId } from "../PdfIdContext";
import { usePdfRequest } from "../PdfRequestContext";
import { addCommentService } from "../Services/commentService";
import { PiChatTeardropTextFill } from "react-icons/pi";
import { RiDeleteBin5Fill } from "react-icons/ri";
import { RxHamburgerMenu } from "react-icons/rx";

const SongList = () => {
  const { pdfId } = usePdfId();
  const { pdfRequest, setPdfRequest } = usePdfRequest();

  const [show, setShow] = useState(false);
  const [comment, setComment] = useState({
    songId: null,
    text: "",
  });
  const [selectedSongId, setSelectedSongId] = useState(null);

  const onDragEnd = (result) => {
    // If dragged outside the list
    if (!result.destination) return;

    const sourceIndex = result.source.index;
    const destinationIndex = result.destination.index;
    // Update pdfRequest with the reordered songs
    setPdfRequest((prevPdfRequest) => {
      const updatedPdfRequest = { ...prevPdfRequest };
      updatedPdfRequest.selectedSongs = reorderArray(
        updatedPdfRequest.selectedSongs,
        sourceIndex,
        destinationIndex
      );
      updatedPdfRequest.comments = reorderArray(
        updatedPdfRequest.comments,
        sourceIndex,
        destinationIndex
      );
      return updatedPdfRequest;
    });
  };

  const reorderArray = (array, startIndex, endIndex) => {
    if (array.length === 0) return [];
    const result = [...array];
    const [removed] = result.splice(startIndex, 1);
    result.splice(endIndex, 0, removed);
    return result;
  };

  const handleCommentChange = (e) => {
    setComment((prevComment) => ({ ...prevComment, text: e.target.value }));
    // setComment({ ...comment, text: e.target.value });
  };

  const addComment = (pdfId, commentRequest) => {
    const updatedPdfRequest = { ...pdfRequest };
    const songIndex = updatedPdfRequest.comments.findIndex(
      (item) => item.songId === commentRequest.songId
    );
    if (songIndex !== -1) {
      updatedPdfRequest.comments[songIndex] = {
        songId: commentRequest.songId,
        text: commentRequest.text,
      };
    } else {
      updatedPdfRequest.comments.push({
        songId: commentRequest.songId,
        text: commentRequest.text,
      });
    }
    setPdfRequest(updatedPdfRequest);
    // If (songIndex !== -1) {}s

    addCommentService(pdfId, commentRequest);

    handleClose();
  };

  const deleteSong = (songId) => {
    const updatedSongs = [...pdfRequest.selectedSongs];
    const updatedComments = [...pdfRequest.comments];
    const songIndex = updatedSongs.findIndex((song) => song.id === songId);
    updatedSongs.splice(songIndex, 1);
    updatedComments.splice(songIndex, 1);
    setPdfRequest({
      ...pdfRequest,
      selectedSongs: updatedSongs,
      comments: updatedComments,
    });
  };

  const handleClose = () => {
    setShow(false);
    setSelectedSongId(null);
    setComment({
      songId: null,
      text: "",
    });
  };

  const handleShow = (songId) => {
    setShow(true);
    setSelectedSongId(songId);

    setComment((prevComment) => ({
      ...prevComment,
      songId: songId,
      text: getCommentBySongId(songId),
    }));
    console.log("song id is", songId);
    console.log("comment is", comment);
  };

  const getTitleBySongId = (songId) => {
    setSelectedSongId(songId);
    const songIndex = pdfRequest.selectedSongs.findIndex(
      (song) => song.id === selectedSongId
    );
    return songIndex !== -1
      ? pdfRequest.selectedSongs[songIndex]?.title || ""
      : "";
  };

  const getCommentBySongId = (songId) => {
    const songIndex = pdfRequest.comments.findIndex(
      (item) => item.songId === songId
    );
    return pdfRequest.comments[songIndex]?.text || "";
  };

  const generateKey = (songId) => {
    return `${songId}_${new Date().getTime()}`;
  };

  return (
    <div className="d-inline-block song-list">
      <DragDropContext onDragEnd={onDragEnd}>
        <Droppable droppableId="song-list">
          {(provided) => (
            <ListGroup
              as="ul"
              {...provided.droppableProps}
              ref={provided.innerRef}
            >
              <Modal show={show} onHide={handleClose}>
                <Modal.Header closeButton>
                  <Modal.Title>Add Comment</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                  <Form>
                    <Form.Group
                      className="mb-3"
                      controlId="exampleForm.ControlInput1"
                    >
                      <Form.Label>Song Title</Form.Label>
                      <Form.Control
                        type="text"
                        value={getTitleBySongId(selectedSongId)}
                        autoFocus
                        disabled
                      />
                    </Form.Group>
                    <Form.Group
                      className="mb-3"
                      controlId="exampleForm.ControlTextarea1"
                    >
                      <Form.Label>Comment</Form.Label>
                      <Form.Control
                        as="textarea"
                        rows={3}
                        onChange={handleCommentChange}
                        value={comment.text}
                      />
                    </Form.Group>
                  </Form>
                </Modal.Body>
                <Modal.Footer>
                  <Button variant="secondary" onClick={() => handleClose()}>
                    Close
                  </Button>
                  <Button
                    variant="primary"
                    onClick={() => addComment(pdfId, comment)}
                  >
                    Save Changes
                  </Button>
                </Modal.Footer>
              </Modal>
              {pdfRequest.selectedSongs.map((song, index) => (
                <Draggable
                  key={generateKey(song.id)}
                  draggableId={song.id.toString()}
                  index={index}
                >
                  {(provided) => (
                    <ListGroup.Item
                      as="li"
                      ref={provided.innerRef}
                      {...provided.draggableProps}
                      {...provided.dragHandleProps}
                    >
                      <div className="d-flex justify-content-between">
                        {index + 1}. &nbsp; {song.title}
                        <div>
                          <PiChatTeardropTextFill
                            className="icon-pointer ms-3"
                            size={22}
                            onClick={() => handleShow(song.id)}
                          />
                          {/* TODO: Add confirmation */}
                          <RiDeleteBin5Fill
                            size={22}
                            className="ms-2 icon-pointer"
                            onClick={() => deleteSong(song.id)}
                          />
                          <RxHamburgerMenu size={22} className="ms-4" />
                        </div>
                      </div>
                    </ListGroup.Item>
                  )}
                </Draggable>
              ))}
              {/* {provided.placeholder} */}
            </ListGroup>
          )}
        </Droppable>
      </DragDropContext>
    </div>
  );
};

export default SongList;
