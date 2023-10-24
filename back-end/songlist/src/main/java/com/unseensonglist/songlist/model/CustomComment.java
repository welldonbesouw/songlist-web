package com.unseensonglist.songlist.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class CustomComment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;
	@Column(columnDefinition = "TEXT")
	private String text;
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "song_id")
	@JsonIgnore //Check this if something's wrong
	private Song song;
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "pdf_id")
	@JsonIgnore
	private Pdf pdf;
	
	public CustomComment() {
	}
	
	public CustomComment(Song song, Pdf pdf, String text) {
		this.song = song;
		this.pdf = pdf;
		this.text = text;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Song getSong() {
		return song;
	}

	public void setSong(Song song) {
		this.song = song;
	}

	public Pdf getPdf() {
		return pdf;
	}

	public void setPdf(Pdf pdf) {
		this.pdf = pdf;
	}

}
