package com.unseensonglist.songlist.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unseensonglist.songlist.model.CustomComment;
import com.unseensonglist.songlist.model.Pdf;
import com.unseensonglist.songlist.model.Song;
import com.unseensonglist.songlist.payload.request.CustomCommentRequest;
import com.unseensonglist.songlist.repository.CustomCommentRepository;
import com.unseensonglist.songlist.repository.PdfRepository;
import com.unseensonglist.songlist.repository.SongRepository;

@Service
public class CustomCommentService {
	
	@Autowired
	CustomCommentRepository commentRepo;
	
	@Autowired
	SongRepository songRepo;
	
	@Autowired
	PdfRepository pdfRepo;
	
	public Optional<CustomComment> findById(Long id) {
		return commentRepo.findById(id);
	}
	
	public Optional<CustomComment> findBySongIdAndPdfId(Long songId, Long pdfId) {
		return commentRepo.findBySongIdAndPdfId(songId, pdfId);
	}

	public CustomComment addComment(Long id, CustomCommentRequest request) {
		Optional<Song> songOpt = songRepo.findById(request.getSongId());
		Optional<Pdf> pdfOpt = pdfRepo.findById(id);
		
		if(songOpt.isPresent() && pdfOpt.isPresent()) {
			Song song = songOpt.get();
			Pdf pdf = pdfOpt.get();
			
			CustomComment comment = commentRepo.findBySongIdAndPdfId(request.getSongId(), id).orElse(new CustomComment());
			comment.setSong(song);
			comment.setPdf(pdf);
			comment.setText(request.getComment());
			
			commentRepo.save(comment);
			
			return comment;
		} else {
			throw new IllegalArgumentException("Song or Pdf not found");
		}
	}

	public Optional<CustomComment> findBySongId(Long songId) {
		return commentRepo.findBySongId(songId);
	}
	
	
}
