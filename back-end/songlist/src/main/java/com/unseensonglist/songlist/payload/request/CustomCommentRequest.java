package com.unseensonglist.songlist.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.unseensonglist.songlist.model.Pdf;
import com.unseensonglist.songlist.model.Song;

public class CustomCommentRequest {

	private Long songId;
	@JsonProperty(value="text") // Need to use this for text property, perhaps a bug
	private String text;

//	public CustomCommentRequest(Long songId, String text) {
//		this.songId = songId;
//		this.text = text;
//	}

	public Long getSongId() {
		return songId;
	}

	public void setSongId(Long songId) {
		this.songId = songId;
	}

	public String getComment() {
		return text;
	}

	public void setComment(String text) {
		this.text = text;
	}

}
