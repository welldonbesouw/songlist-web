package com.unseensonglist.songlist.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.unseensonglist.songlist.model.Song;
import com.unseensonglist.songlist.payload.request.SongDto;
import com.unseensonglist.songlist.service.SongService;

@RestController
@RequestMapping("/api/songs")
public class SongController {

	@Autowired
	SongService songService;

	@PostMapping("")
	public ResponseEntity<Song> registerSong(@RequestBody SongDto songDto) {
		Song song = songService.saveSong(songDto);

		return ResponseEntity.ok(song);
	}

	@GetMapping("{songId}")
	public ResponseEntity<?> getSong(@PathVariable Long songId) {
		Optional<Song> songOpt = songService.findSong(songId);

		return ResponseEntity.ok(songOpt.orElse(new Song()));
	}

	@GetMapping("/search")
	public ResponseEntity<Page<Song>> searchSong(@RequestParam("keyword") String keyword,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "9") int size) {

		Pageable pageable = PageRequest.of(page, size);
		Page<Song> songs;

		if (keyword != null && !keyword.isEmpty()) {
			songs = songService.searchSong(keyword, pageable);
		} else {
			songs = songService.getAllSong(pageable);
		}

		return ResponseEntity.ok(songs);
	}

}
