import { createContext, useContext, useState } from "react";

const PdfRequestContext = createContext();

export const PdfRequestProvider = ({ children }) => {
  const [pdfRequest, setPdfRequest] = useState({
    selectedSongs: [],
    comments: [],
    customizationOptions: {
      titleOne: "",
      titleTwo: "",
      titleThree: "",
      titleOneSize: 16,
      titleTwoSize: 16,
      titleThreeSize: 16,
      margin: 50,
      fontSize: 12,
      lineSpacing: 16,
    },
  });

  return (
    <PdfRequestContext.Provider value={{ pdfRequest, setPdfRequest }}>
      {children}
    </PdfRequestContext.Provider>
  );
};

export const usePdfRequest = () => {
  return useContext(PdfRequestContext);
};
