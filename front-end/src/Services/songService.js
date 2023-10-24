import axios from "axios";

const API_URL = "/api/songs";

function searchService(keyword, currentPage, pageSize) {
  return axios.get(
    API_URL + `/search?keyword=${keyword}&page=${currentPage}&size=${pageSize}`
  );
}

function viewService(songId) {
  return axios.get(API_URL + `/${songId}`);
}

export { searchService, viewService };
