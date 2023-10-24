import axios from "axios";

const API_URL = "/api/pdf";

function createService() {
  return axios.post(API_URL + "/createPdf");
}

function generateService(pdfId, generatePdfRequest) {
  return axios.post(API_URL + `/generatePdf/${pdfId}`, generatePdfRequest);
}

function savePdfService(pdfId, generatePdfRequest) {
  return axios.post(API_URL + "/savePdf", pdfId, generatePdfRequest);
}

function downloadService(fileName) {
  return axios.get(API_URL + `/downloadPdf/${fileName}`);
}

export { createService, generateService, savePdfService, downloadService };
