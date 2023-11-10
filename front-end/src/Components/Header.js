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
          <Navbar.Brand
            href="/"
            className="fs-4 me-4 d-flex align-items-center"
          >
            <img
              alt=""
              src={Logo}
              width="37"
              height="37"
              className="d-inline-block align-top logo me-3"
            />
            <div className="d-flex flex-column justify-content-center">
              <h5 className="mb-0">UnseenSonglist</h5>
              <p className="version-text m-0">Version 1.0.0</p>
            </div>
          </Navbar.Brand>
          <SearchBar />
        </Container>
      </Navbar>
    </>
  );
};

export default Header;
