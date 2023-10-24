package com.unseensonglist.songlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.unseensonglist.songlist.model.Pdf;

public interface PdfRepository extends JpaRepository<Pdf, Long> {

}
