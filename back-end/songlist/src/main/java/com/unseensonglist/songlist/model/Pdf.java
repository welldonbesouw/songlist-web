package com.unseensonglist.songlist.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Pdf {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String title;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "pdf_song",
			joinColumns = @JoinColumn(name = "pdf_id"),
			inverseJoinColumns = @JoinColumn(name = "song_id"))
	private List<Song> selectedSongs = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Song> getSelectedSongs() {
		return selectedSongs;
	}

	public void setSelectedSongs(List<Song> selectedSongs) {
		this.selectedSongs = selectedSongs;
	}

}
