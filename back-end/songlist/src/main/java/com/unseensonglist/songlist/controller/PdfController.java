package com.unseensonglist.songlist.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.unseensonglist.songlist.model.CustomComment;
import com.unseensonglist.songlist.model.Pdf;
import com.unseensonglist.songlist.model.Song;
import com.unseensonglist.songlist.payload.request.CustomCommentRequest;
import com.unseensonglist.songlist.payload.request.GeneratePdfRequest;
import com.unseensonglist.songlist.payload.request.PdfCustomizationOptions;
import com.unseensonglist.songlist.service.PdfService;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

	@Autowired
	PdfService pdfService;
	
	@PostMapping("/createPdf")
	public ResponseEntity<Pdf> createPdf() {
		Pdf pdf = pdfService.createPdf();
		
		return ResponseEntity.ok(pdf);
	}

	@PostMapping("/generatePdf/{pdfId}")
	public ResponseEntity<String> generatePdf(@RequestBody GeneratePdfRequest request, @PathVariable Long pdfId) {
		List<Song> selectedSongs = request.getSelectedSongs();
		List<CustomCommentRequest> comments = request.getComments();
		PdfCustomizationOptions options = request.getCustomizationOptions();
		
		byte[] pdfData = pdfService.generateSongList(selectedSongs, comments, options, pdfId);
		
		if(pdfData != null) {
			String encodedPdf = Base64.getEncoder().encodeToString(pdfData);
			
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.TEXT_PLAIN);
			return ResponseEntity.ok().headers(headers).body(encodedPdf);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@PostMapping("/savePdf")
	public ResponseEntity<String> savePdf(@PathVariable Long id, @RequestBody GeneratePdfRequest request) {
		List<Song> selectedSongs = request.getSelectedSongs();
		List<CustomCommentRequest> comments = request.getComments();
		PdfCustomizationOptions options = request.getCustomizationOptions();
		
		String fileName = pdfService.savePdf(id, selectedSongs, comments, options);
		
		if(fileName != null) {
			return ResponseEntity.ok(fileName);
		} else {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Saving PDF failed");
		}
	}

	@GetMapping("/download/{fileName}")
	public ResponseEntity<Resource> downloadPdf(@PathVariable String fileName) {
		try {
			// Define the path to the "temp-pdfs" folder
			Path filePath = Paths.get("temp-pdfs", fileName);
			Resource resource = new UrlResource(filePath.toUri());

			if (resource.exists()) {
				HttpHeaders headers = new HttpHeaders();
				headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
				return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(resource);
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
//	@GetMapping("/generatePdf")
//	public ResponseEntity<String> generatePdf(@RequestBody GeneratePdfRequest request) {
//		List<Song> selectedSongs = request.getSelectedSongs();
//		List<CustomComment> comments =request.getComments();
//		PdfCustomizationOptions customizationOptions = request.getCustomizationOptions();
//
//		String fileName = pdfService.generateSongList(selectedSongs, comments, customizationOptions);
//
//		if (fileName != null) {
//			return ResponseEntity.ok(fileName);
//		} else {
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("PDF generation failed.");
//		}
//	}
//
//	@GetMapping("/previewPdf/{fileName}")
//	public ResponseEntity<InputStreamResource> previewPdf(@PathVariable String fileName) {
//		try {
//			// Construct the full path to the PDF file
//			Path filePath = Paths.get(pdfStoragePath, fileName);
//			File pdfFile = filePath.toFile();
//
//			if (pdfFile.exists()) {
//				InputStreamResource resource = new InputStreamResource(new FileInputStream(pdfFile));
//
//				HttpHeaders headers = new HttpHeaders();
//				headers.setContentType(MediaType.APPLICATION_PDF);
//
//				return ResponseEntity.ok().headers(headers).contentLength(pdfFile.length()).body(resource);
//			} else {
//				return ResponseEntity.notFound().build();
//			}
//		} catch (IOException e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//		}
//	}
}
