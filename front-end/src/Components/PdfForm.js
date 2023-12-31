import React, { useState } from "react";
import { Button, Card, Col, Form, InputGroup, Row } from "react-bootstrap";
import { usePdfRequest } from "../PdfRequestContext";
import { TiTick } from "react-icons/ti";

const PdfForm = (validated) => {

  const [isApplied, setIsApplied] = useState(false);
  const [titleOneWarning, setTitleOneWarning] = useState(false);

  const { pdfRequest, setPdfRequest, customization, setCustomization } =
    usePdfRequest();

  const handleSubmit = (e) => {
    e.preventDefault();
    if (customization.titleOne !== "") {
      const form = e.currentTarget;
      if (form.checkValidity() === false) {
        e.stopPropagation();
      }
      setPdfRequest({ ...pdfRequest, customizationOptions: customization });
      validated = true;
      showAppliedText();
      setTitleOneWarning(false);
    } else {
      setTitleOneWarning(true);
    }
  };

  const handleValueChange = (e, line) => {
    setCustomization(() => ({
      ...customization,
      [line]: e.target.value,
    }));
  };

  const showAppliedText = () => {
    setIsApplied(true);
    setTimeout(() => {
      setIsApplied(false);
    }, 3000);
  };

  return (
    <>
      <Card className="mt-3 mb-2" style={{ maxWidth: "45rem" }}>
        <Form
          noValidate
          validated={validated}
          onSubmit={handleSubmit}
          className="mt-4 mx-3 mb-2"
        >
          {/* Title Form */}
          <Form.Group as={Row} controlId="formPdfTitle">
            <Card.Title as={"h6"}>Title</Card.Title>
          </Form.Group>
          <Form.Group as={Row} className="ms-1">
            <Col xs={8} md={9}>
              <Form.Label htmlFor="titleOne" visuallyHidden>
                Line 1
              </Form.Label>
              <InputGroup hasValidation className="mb-2">
                <InputGroup.Text>Line 1</InputGroup.Text>
                <Form.Control
                  id="titleOne"
                  type="text"
                  placeholder="Susunan Lagu"
                  onChange={(e) => handleValueChange(e, "titleOne")}
                  required
                />
                <Form.Control.Feedback type="invalid">
                  Please enter a title.
                </Form.Control.Feedback>
              </InputGroup>
            </Col>
            <Col xs={4} md={3}>
              <Form.Label htmlFor="titleOneSize" visuallyHidden>
                Size 1
              </Form.Label>
              <InputGroup hasValidation className="mb-2">
                <InputGroup.Text>Size</InputGroup.Text>
                <Form.Control
                  id="titleOneSize"
                  type="tel"
                  pattern="\b([1-9]|[1-9][0-9])\b"
                  placeholder="16"
                  defaultValue={16}
                  onChange={(e) => handleValueChange(e, "titleOneSize")}
                />
                <Form.Control.Feedback type="invalid">
                  Valid input is 1 to 99
                </Form.Control.Feedback>
              </InputGroup>
            </Col>
          </Form.Group>
          <Form.Group as={Row} className="ms-1">
            <Col xs={8} md={9}>
              <Form.Label htmlFor="titleTwo" visuallyHidden>
                Line 2
              </Form.Label>
              <InputGroup className="mb-2">
                <InputGroup.Text>Line 2</InputGroup.Text>
                <Form.Control
                  id="titleTwo"
                  type="text"
                  placeholder="Second line of title"
                  defaultValue={""}
                  onChange={(e) => handleValueChange(e, "titleTwo")}
                />
              </InputGroup>
            </Col>
            <Col xs={4} md={3}>
              <Form.Label htmlFor="titleTwoSize" visuallyHidden>
                Size 2
              </Form.Label>
              <InputGroup hasValidation className="mb-2">
                <InputGroup.Text>Size</InputGroup.Text>
                <Form.Control
                  id="titleTwoSize"
                  type="tel"
                  pattern="\b([1-9]|[1-9][0-9])\b"
                  placeholder="16"
                  defaultValue={16}
                  onChange={(e) => handleValueChange(e, "titleTwoSize")}
                />
                <Form.Control.Feedback type="invalid">
                  Valid input is 1 to 99
                </Form.Control.Feedback>
              </InputGroup>
            </Col>
          </Form.Group>
          <Form.Group as={Row} className="ms-1 mb-2">
            <Col xs={8} md={9}>
              <Form.Label htmlFor="titleThree" visuallyHidden>
                Line 3
              </Form.Label>
              <InputGroup className="mb-2">
                <InputGroup.Text>Line 3</InputGroup.Text>
                <Form.Control
                  id="titleThree"
                  type="text"
                  placeholder="Third line of title"
                  defaultValue={""}
                  onChange={(e) => handleValueChange(e, "titleThree")}
                />
              </InputGroup>
            </Col>
            <Col xs={4} md={3}>
              <Form.Label htmlFor="titleThreeSize" visuallyHidden>
                Size 3
              </Form.Label>
              <InputGroup hasValidation>
                <InputGroup.Text>Size</InputGroup.Text>
                <Form.Control
                  id="titleThreeSize"
                  type="tel"
                  pattern="\b([1-9]|[1-9][0-9])\b"
                  placeholder="16"
                  defaultValue={16}
                  onChange={(e) => handleValueChange(e, "titleThreeSize")}
                />
                <Form.Control.Feedback type="invalid">
                  Valid input is 1 to 99
                </Form.Control.Feedback>
              </InputGroup>
            </Col>
          </Form.Group>
          <Form.Group as={Row}>
            <Form.Label column xs={3} md={2} lg={2} htmlFor="margin">
              <Card.Title as={"h6"}>Margin</Card.Title>
            </Form.Label>
            <Col xs={3} md={2}>
              <InputGroup hasValidation className="mb-2">
                <Form.Control
                  id="margin"
                  type="tel"
                  pattern="\b([1-9]|[1-6][0-9]|70)\b"
                  placeholder="50"
                  defaultValue={50}
                  onChange={(e) => handleValueChange(e, "margin")}
                />
                <Form.Control.Feedback type="invalid">
                  Valid input is 1 to 70
                </Form.Control.Feedback>
              </InputGroup>
            </Col>
          </Form.Group>
          <Form.Group as={Row}>
            <Form.Label column xs={3} md={2} lg={2} htmlFor="fontSize">
              <Card.Title as={"h6"}>Font Size</Card.Title>
            </Form.Label>
            <Col xs={3} md={2}>
              <InputGroup hasValidation className="mb-2">
                <Form.Control
                  id="fontSize"
                  type="tel"
                  pattern="\b([1-9]|[1-4][0-9]|50)\b"
                  placeholder="14"
                  defaultValue={14}
                  onChange={(e) => handleValueChange(e, "fontSize")}
                />
                <Form.Control.Feedback type="invalid">
                  Valid input is 1 to 50
                </Form.Control.Feedback>
              </InputGroup>
            </Col>
          </Form.Group>
          <Form.Group as={Row} className="mb-3">
            <Form.Label column xs={3} md={2} lg={2} htmlFor="spacing">
              <Card.Title as={"h6"}>Spacing</Card.Title>
            </Form.Label>
            <Col xs={3} md={2}>
              <InputGroup hasValidation className="mb-1">
                <Form.Control
                  id="spacing"
                  type="tel"
                  pattern="\b([1-9]|[1-9][0-9]|100)\b"
                  placeholder="16"
                  defaultValue={16}
                  onChange={(e) => handleValueChange(e, "lineSpacing")}
                />
                <Form.Control.Feedback type="invalid">
                  Valid input is 1 to 100
                </Form.Control.Feedback>
              </InputGroup>
            </Col>
          </Form.Group>
          <div className="d-flex justify-content-end align-items-center">
            {isApplied ? (
              <h6 className="text-success me-3">
                <TiTick lg />
                Applied
              </h6>
            ) : (
              <></>
            )}
            {titleOneWarning ? (
              <h6 className="text-danger me-3">
                Title at line 1 must not be empty!
              </h6>
            ) : (
              <></>
            )}
            <Button
              className="mb-2 apply-button"
              type="submit"
              variant="dark"
              style={{ width: "8rem" }}
            >
              Apply
            </Button>
          </div>
        </Form>
      </Card>
    </>
  );
};

export default PdfForm;
