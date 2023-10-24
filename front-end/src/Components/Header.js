import React from "react";
import { Container, Navbar } from "react-bootstrap";
import Logo from "../assets/unseen-logo-4().png";
import SearchBar from "./SearchBar";

const Header = () => {
  return (
    <>
      <Navbar
        bg="light"
        className="bg-body-tertiary navbar-shadow"
        data-bs-theme="light"
      >
        <Container>
          <Navbar.Brand href="/" className="fs-4 me-4">
            <img
              alt=""
              src={Logo}
              width="37"
              height="37"
              className="d-inline-block align-top logo me-3"
            />
            UnseenPdf
          </Navbar.Brand>
          <SearchBar />
        </Container>
      </Navbar>
    </>
  );
};

export default Header;
