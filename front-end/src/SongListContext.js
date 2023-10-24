import { createContext, useContext, useState } from "react";

const SongListContext = createContext();

export const SongListProvider = ({ children }) => {
  const [songList, setSongList] = useState({
    song: null,
    comment: {
      songId: null,
      text: "",
    },
  });

  return (
    <SongListContext.Provider value={{ songList, setSongList }}>
      {children}
    </SongListContext.Provider>
  );
};

export const useSongList = () => {
  return useContext(SongListContext);
};
