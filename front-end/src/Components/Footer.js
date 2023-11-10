import React from "react";
import { Container } from "react-bootstrap";

const Footer = () => {
  return (
    <div className="bg-dark footer">
      <Container>
        <div className="d-flex justify-content-center p-1">
          <p className="text-light m-0">
            &copy;2023 Welldon Besouw. All rights reserved.
          </p>
        </div>
      </Container>
    </div>
  );
};

export default Footer;
