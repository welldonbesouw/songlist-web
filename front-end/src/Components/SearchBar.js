import React, { useState } from "react";
import { Button, Form, InputGroup } from "react-bootstrap";
import { TbMusicSearch } from "react-icons/tb";
import { useNavigate } from "react-router-dom";
import { usePdfId } from "../PdfIdContext";

const SearchBar = () => {
  const [keyword, setKeyword] = useState("");

  const { pdfId } = usePdfId();

  const navigate = useNavigate();

  const searchSong = () => {
    if (pdfId !== null && pdfId !== undefined) {
      navigate(`/createPdf/${pdfId}/${keyword}`);
    } else {
      navigate(`/${keyword}`);
    }
  };

  return (
    <>
      <InputGroup className="my-1">
        <Form.Control
          aria-label="Search song"
          placeholder="Search song"
          onChange={(e) => setKeyword(e.target.value)}
          value={keyword}
          onKeyDown={(e) => {
            if (e.key === "Enter") searchSong();
          }}
        />
        <InputGroup.Text>
          <Button onClick={() => searchSong()} variant="dark">
            <TbMusicSearch />
          </Button>
        </InputGroup.Text>
      </InputGroup>
    </>
  );
};

export default SearchBar;
