import React, { useEffect, useRef, useState } from "react";
import Browse from "./browse";
import { Link, useParams } from "react-router-dom";
import {
  Button,
  Card,
  Col,
  Container,
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
import { TbArrowBigRightLineFilled } from "react-icons/tb";

const CreatePdf = () => {
  // !!important
//  pdfjs.GlobalWorkerOptions.workerSrc = new URL(
//    "pdfjs-dist/build/pdf.worker.min.js",
//    import.meta.url
//  ).toString();

pdfjs.GlobalWorkerOptions.workerSrc = `//unpkg.com/pdfjs-dist@${pdfjs.version}/legacy/build/pdf.worker.min.js`;

  const [pdfData, setPdfData] = useState(null);
  const [fileName, setFileName] = useState("");
  const [numPages, setNumPages] = useState(null);
  const [pageNumber, setPageNumber] = useState(1);
  // const [loading, setLoading] = useState(false);
  const [isPdfSaved, setIsPdfSaved] = useState(false);
  const [isGenerateDisabled, setIsGenerateDisabled] = useState(true);

  const { pdfRequest, customization } = usePdfRequest();
  const { pdfId } = useParams();
  const { setPdfId } = usePdfId();

  const ref = useRef(null);

  useEffect(() => {
    setPdfId(pdfId);
  }, [pdfId, setPdfId]);

  useEffect(() => {
    if (customization.titleOne !== "") setIsGenerateDisabled(false);
    else setIsGenerateDisabled(true);
  }, [customization.titleOne]);

  const handleGenerate = async () => {
    // Send pdfRequest as the payload
    try {
      const response = await generateService(pdfId, pdfRequest);

      setPdfData(response.data);
    } catch (error) {
      console.error("Error generating PDF preview", error);
    }
  };

  const onDocumentLoadSuccess = ({ numPages }) => {
    try {
      setNumPages(numPages);
    } catch (error) {
      console.log("Error occurred.");
    }
  };

  const handleSave = () => {
    try {
      // Send pdfRequest as the payload
      savePdfService(pdfId, pdfRequest).then((response) => {
        setFileName(response.data);
      });
      enableDownload();
    } catch (error) {
      console.error("Error saving PDF:", error);
    }
  };

  const enableDownload = () => {
    setIsPdfSaved(true);
  };

  const disableDownload = () => {
    setIsPdfSaved(false);
  };

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
              <Card.Body className="pb-1">
                <SongList ref={ref} />
                {pdfRequest.selectedSongs.length === 0 ? (
                  <h5 className="add-songs">Let's add some songs!</h5>
                ) : (
                  <></>
                )}

                <Button
                  className="mt-3"
                  variant="dark"
                  onClick={() => handleGenerate()}
                  disabled={isGenerateDisabled}
                >
                  Generate PDF
                </Button>
                <Button
                  className="mt-3 ms-3"
                  variant="dark"
                  onClick={() => handleSave()}
                  disabled={isGenerateDisabled}
                >
                  Save PDF
                </Button>
                {isPdfSaved ? (
                  <Link
                    to={`http://localhost:8080/api/pdf/download/${fileName}`}
                    download
                    onClick={() => disableDownload()}
                  >
                    <Button className="mt-3 ms-3" variant="dark">
                      Download PDF
                    </Button>
                  </Link>
                ) : (
                  <></>
                )}
                <p className="mt-1 mb-1 text-danger instruction">
                  *Generate
                  <TbArrowBigRightLineFilled className="arrow-pos me-1 ms-1" />
                  Save <TbArrowBigRightLineFilled className="arrow-pos me-1" />
                  Download
                </p>
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
