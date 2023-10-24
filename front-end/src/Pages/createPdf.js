import React, { useEffect, useState } from "react";
import Browse from "./browse";
import { useParams } from "react-router-dom";
import {
  Button,
  Card,
  Col,
  Container,
  Form,
  InputGroup,
  Pagination,
  Row,
  Spinner,
} from "react-bootstrap";
import { usePdfId } from "../PdfIdContext";
import { generateService, savePdfService } from "../Services/pdfService";
import SongList from "../Components/SongList";
import { usePdfRequest } from "../PdfRequestContext";
import { Document, Page, pdfjs } from "react-pdf";
import PdfForm from "../Components/PdfForm";

const CreatePdf = () => {
  // !!important
  pdfjs.GlobalWorkerOptions.workerSrc = new URL(
    "pdfjs-dist/build/pdf.worker.min.js",
    import.meta.url
  ).toString();

  const [pdfData, setPdfData] = useState(null);
  const [fileName, setFileName] = useState("");
  const [numPages, setNumPages] = useState(null);
  const [pageNumber, setPageNumber] = useState(1);
  const [loading, setLoading] = useState(false);
  const [validated, setValidated] = useState(false);

  const { pdfRequest, setPdfRequest } = usePdfRequest();
  const { pdfId } = useParams();
  const { setPdfId } = usePdfId();

  useEffect(() => {
    setPdfId(pdfId);
  }, [pdfId, setPdfId]);

  useEffect(() => {
    if (pdfData) {
      setLoading(true);
    }
    console.log(pdfData);
    console.log(loading);
  }, [pdfData, loading]);

  const handleGenerate = async (e) => {
    // Send pdfRequest as the payload
    try {
      const delayedResponse = () =>
        new Promise((resolve) => setTimeout(resolve, 1000));
      await delayedResponse();

      const response = await generateService(pdfId, pdfRequest);

      setPdfData(response.data);
      console.log("pdfData is", pdfData);
    } catch (error) {
      console.error("Error generating PDF preview", error);
    }
  };

  const onDocumentLoadSuccess = ({ numPages }) => {
    try {
      console.log("loaded pdf data is:", pdfData);
      setNumPages(numPages);
    } catch (error) {
      console.log("Error occurred.");
    }
  };

  const handleSave = () => {
    try {
      // Send pdfRequest as the payload
      savePdfService(pdfRequest).then((response) => {
        setFileName(response.data);
      });
    } catch (error) {
      console.error("Error saving PDF:", error);
    }
  };

  const handleDownload = () => {};

  const handleNextPage = () => {
    if (pageNumber < numPages) {
      setPageNumber(pageNumber + 1);
    }
  };

  const handlePrevPage = () => {
    if (pageNumber > 1) {
      setPageNumber(pageNumber - 1);
    }
  };

  return (
    <>
      <Browse />
      <Container>
        {/* TODO: Make this form and songlist side by side with pdf display */}
        <PdfForm />
        <Row>
          <Col sm={12} lg={6}>
            <Card className="mb-4">
              <Card.Header as={"h4"}>Song List</Card.Header>
              <Card.Body>
                <SongList />
                {/* <h1>PDF Generator</h1> */}
                <Button
                  className="mt-3"
                  variant="dark"
                  onClick={() => handleGenerate()}
                >
                  Generate PDF
                </Button>
                <Button
                  className="mt-3 ms-3"
                  variant="dark"
                  onClick={() => handleSave()}
                >
                  Save PDF
                </Button>
              </Card.Body>
            </Card>
          </Col>
          <Col sm={12} lg={6}>
            {pdfData !== null ? (
              <>
                {/* Display the PDF using a PDF viewer component */}
                <div className="">
                  <Document
                    file={`data:application/pdf;base64,${pdfData}`}
                    // file={{ data: decodedPdfData }}
                    // file="/Users/welldonbesouw/Documents/FLK.pdf"
                    onLoadSuccess={onDocumentLoadSuccess}
                    onLoadError={(error) =>
                      console.error("PDF loading error", error)
                    }
                  >
                    <Page
                      pageNumber={pageNumber}
                      renderTextLayer={false}
                      renderAnnotationLayer={false}
                      customTextRenderer={false}
                    />
                  </Document>
                  <div>
                    <Pagination>
                      <Pagination.Prev
                        onClick={() => handlePrevPage()}
                        disabled={pageNumber <= 1}
                      >
                        Previous Page
                      </Pagination.Prev>
                      <span className="mx-2">
                        Page {pageNumber} of {numPages}
                      </span>
                      <Pagination.Next
                        onClick={() => handleNextPage()}
                        disabled={pageNumber >= numPages}
                      >
                        Next Page
                      </Pagination.Next>
                    </Pagination>
                  </div>
                </div>
                <Button onClick={handleDownload}>Download PDF</Button>
              </>
            ) : (
              <>
                <Spinner animation="border" variant="light" />
              </>
            )}
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default CreatePdf;
