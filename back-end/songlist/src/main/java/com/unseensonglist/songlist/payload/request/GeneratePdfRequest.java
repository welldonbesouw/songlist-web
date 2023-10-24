package com.unseensonglist.songlist.payload.request;

import java.util.ArrayList;
import java.util.List;

import com.unseensonglist.songlist.model.CustomComment;
import com.unseensonglist.songlist.model.Song;

public class GeneratePdfRequest {

	private List<Song> selectedSongs = new ArrayList<>();
	private List<CustomCommentRequest> comments = new ArrayList<>();
	private PdfCustomizationOptions customizationOptions;

	public List<Song> getSelectedSongs() {
		return selectedSongs;
	}

	public void setSelectedSongs(List<Song> selectedSongs) {
		this.selectedSongs = selectedSongs;
	}

	public List<CustomCommentRequest> getComments() {
		return comments;
	}

	public void setComments(List<CustomCommentRequest> comments) {
		this.comments = comments;
	}

	public PdfCustomizationOptions getCustomizationOptions() {
		return customizationOptions;
	}

	public void setCustomizationOptions(PdfCustomizationOptions customizationOptions) {
		this.customizationOptions = customizationOptions;
	}

}
