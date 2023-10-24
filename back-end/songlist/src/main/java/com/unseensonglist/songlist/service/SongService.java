package com.unseensonglist.songlist.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.unseensonglist.songlist.model.Song;
import com.unseensonglist.songlist.payload.request.SongDto;
import com.unseensonglist.songlist.repository.SongRepository;

@Service
public class SongService {
	
	@Autowired
	SongRepository songRepo;
	
	public Song saveSong(SongDto songDto) {
		Song song = new Song();
		song.setTitle(songDto.getTitle());
		song.setLyrics(songDto.getLyrics());
		song.setKey(songDto.getKey());
		
		return songRepo.save(song);
	}

	public Optional<Song> findSong(Long songId) {
		return songRepo.findById(songId);
	}

	public Page<Song> searchSong(String keyword, Pageable pageable) {
		Set<Song> uniqueSongs = new HashSet<>();
		
		Page<Song> songsByTitle = songRepo.findByTitleContainingIgnoreCase(keyword, pageable);
		uniqueSongs.addAll(songsByTitle.getContent());
		
		Page<Song> songsByLyrics = songRepo.findByLyricsContainingIgnoreCase(keyword, pageable);
		uniqueSongs.addAll(songsByLyrics.getContent());
		
		List<Song> combinedSongs = new ArrayList<>(uniqueSongs);
		int totalResults = uniqueSongs.size();
		
		return new PageImpl<>(combinedSongs, pageable, totalResults);
	}

	public Page<Song> getAllSong(Pageable pageable) {
		return songRepo.findAll(pageable);
	}
}
