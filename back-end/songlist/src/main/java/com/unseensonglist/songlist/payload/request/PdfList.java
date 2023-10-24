package com.unseensonglist.songlist.payload.request;

import java.util.ArrayList;
import java.util.List;

import com.unseensonglist.songlist.model.Song;

public class PdfList {

	private String title;
	private List<Song> selectedSongs = new ArrayList<>();

	public PdfList() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Song> getSong() {
		return selectedSongs;
	}

	public void setSong(List<Song> selectedSongs) {
		this.selectedSongs = selectedSongs;
	}

}
