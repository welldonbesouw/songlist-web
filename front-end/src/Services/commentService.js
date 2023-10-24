import axios from "axios";

const API_URL = "/api/comments";

function addCommentService(pdfId, request) {
  return axios.post(API_URL + `/${pdfId}`, request);
}

function viewCommentService(songId) {
  return axios.get(API_URL, songId);
}

export { addCommentService, viewCommentService };
