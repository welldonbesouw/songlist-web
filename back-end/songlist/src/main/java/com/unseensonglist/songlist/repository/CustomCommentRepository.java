package com.unseensonglist.songlist.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unseensonglist.songlist.model.CustomComment;

public interface CustomCommentRepository extends JpaRepository<CustomComment, Long> {

	Optional<CustomComment> findBySongIdAndPdfId(Long songId, Long pdfId);

	Optional<CustomComment> findBySongId(Long songId);

}
