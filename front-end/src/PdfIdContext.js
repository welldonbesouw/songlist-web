import { createContext, useContext, useState } from "react";

const PdfIdContext = createContext();

export const PdfIdProvider = ({ children }) => {
  const [pdfId, setPdfId] = useState(null);

  return (
    <PdfIdContext.Provider value={{ pdfId, setPdfId }}>
      {children}
    </PdfIdContext.Provider>
  );
};

export const usePdfId = () => {
  return useContext(PdfIdContext);
};
