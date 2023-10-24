package com.unseensonglist.songlist.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.unseensonglist.songlist.model.Song;

public interface SongRepository extends JpaRepository<Song, Long>{

	Page<Song> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);

	Page<Song> findByLyricsContainingIgnoreCase(String keyword, Pageable pageable);
	
}
