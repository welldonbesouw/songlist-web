package com.unseensonglist.songlist.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unseensonglist.songlist.model.CustomComment;
import com.unseensonglist.songlist.payload.request.CustomCommentRequest;
import com.unseensonglist.songlist.service.CustomCommentService;

@RestController
@RequestMapping("/api/comments")
public class CustomCommentController {
	
	@Autowired
	CustomCommentService commentService;
	
	@PostMapping("{pdfId}")
	public ResponseEntity<CustomComment> addComment(@PathVariable Long pdfId, @RequestBody CustomCommentRequest request) {
		CustomComment comment = commentService.addComment(pdfId, request);
		
		return ResponseEntity.ok(comment);
	}
	
	@GetMapping("")
	public ResponseEntity<String> viewComment(Long songId) {
		Optional<CustomComment> comment = commentService.findBySongId(songId);
		String text = comment.get().getText();
		
		return ResponseEntity.ok(text);
	}
}
