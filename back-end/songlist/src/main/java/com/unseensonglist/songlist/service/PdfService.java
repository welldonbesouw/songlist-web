package com.unseensonglist.songlist.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.swing.text.AbstractDocument.Content;

import org.apache.pdfbox.contentstream.PDContentStream;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.unseensonglist.songlist.model.CustomComment;
import com.unseensonglist.songlist.model.Pdf;
import com.unseensonglist.songlist.model.Song;
import com.unseensonglist.songlist.payload.request.CustomCommentRequest;
import com.unseensonglist.songlist.payload.request.PdfCustomizationOptions;
import com.unseensonglist.songlist.repository.CustomCommentRepository;
import com.unseensonglist.songlist.repository.PdfRepository;

@Service
public class PdfService {

	@Autowired
	PdfRepository pdfRepo;

	@Autowired
	CustomCommentRepository commentRepo;

	@Autowired
	CustomCommentService commentService;

	public Pdf createPdf() {
		Pdf pdf = new Pdf();
		pdf.setTitle("Untitled");
		return pdfRepo.save(pdf);
	}

	public byte[] generateSongList(List<Song> selectedSongs, List<CustomCommentRequest> comments,
			PdfCustomizationOptions options, Long pdfId) {

		try {
			PDDocument document = createPdfDocument(selectedSongs, comments, options, pdfId);

			// Convert the PDF document to a byte array
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			document.save(byteArrayOutputStream);
			document.close();

			return byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public String savePdf(Long id, List<Song> selectedSongs, List<CustomCommentRequest> comments,
			PdfCustomizationOptions options) {
		try {
			PDDocument document = createPdfDocument(selectedSongs, comments, options, id);

			// Generate a unique filename for the PDF
			String fileName = "SongList_" + System.currentTimeMillis() + ".pdf";

			// Define the path where the PDF will be saved temporarily
			String filePath = "/Users/welldonbesouw/Documents/songlist-repo/temp-pdfs/" + fileName;

			// Save the PDF to the defined path
			document.save(filePath);
			document.close();

			Optional<Pdf> pdf = pdfRepo.findById(id);
			pdf.get().setTitle(fileName);

			// Return the filename to be used for downloading
			return fileName;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	public PDDocument createPdfDocument(List<Song> selectedSongs, List<CustomCommentRequest> comments,
			PdfCustomizationOptions options, Long pdfId) throws IOException {
		PDDocument document = new PDDocument();
		PDPage page = new PDPage(PDRectangle.A4);
		document.addPage(page);
		PDFont fontBold = PDType1Font.HELVETICA_BOLD;
		PDFont fontRegular = PDType1Font.HELVETICA; 
		PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;

		PDPageContentStream contentStream = new PDPageContentStream(document, page);
		float margin = options.getMargin();
		float yStart = page.getMediaBox().getHeight() - margin;
		float yPosition = yStart;
		int songNumber = 1;

		// Number of columns
		float xPosition = margin;
		float xPosition2 = page.getMediaBox().getWidth() / 2 + 25;
		float rightMargin = 0;
		int column = 1;
		
		// Write pdf title
		String titleOne = options.getTitleOne();
		String titleTwo = options.getTitleTwo();
		String titleThree = options.getTitleThree();
		
		float titleOneWidth = fontBold.getStringWidth(titleOne) / 1000 * options.getTitleOneSize();
		float titleTwoWidth = fontBold.getStringWidth(titleTwo) / 1000 * options.getTitleTwoSize();
		float titleThreeWidth = fontBold.getStringWidth(titleThree) / 1000 * options.getTitleThreeSize();
		
		float titleOneHeight = fontBold.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * options.getTitleOneSize();
		float titleTwoHeight = fontBold.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * options.getTitleTwoSize();
		float titleThreeHeight = fontBold.getFontDescriptor().getFontBoundingBox().getHeight() / 1000 * options.getTitleThreeSize();
		float x = (page.getMediaBox().getWidth() - titleOneWidth) / 2;
		float y = page.getMediaBox().getHeight() - margin - titleOneHeight;

		startText(contentStream, options.getTitleOneSize(), x, y, fontBold, options.getTitleOneSize());
		contentStream.showText(titleOne);
		yPosition -= options.getTitleOneSize();
		contentStream.endText();
		x = (x * 2 + titleOneWidth - titleTwoWidth) / 2;
		y -= titleTwoHeight + 5;
		
		startText(contentStream, options.getTitleTwoSize(), x, y, fontBold, options.getTitleTwoSize());
		contentStream.showText(titleTwo);
		if(titleTwo != "") yPosition -= options.getTitleTwoSize();
		contentStream.endText();
		x = (x * 2 + titleTwoWidth - titleThreeWidth) / 2;
		y -= titleThreeHeight + 5;
		
		startText(contentStream, options.getTitleThreeSize(), x, y, fontBold, options.getTitleThreeSize());
		contentStream.showText(titleThree);
		if(titleThree != "") yPosition -= options.getTitleThreeSize();
		contentStream.endText();
		
		// If only titleOne filled, reduce the spacing with the songs
		if(titleTwo == "" && titleThree == "") yPosition -= 50;
		else yPosition -= 70;
		
		float yFirstPage = yPosition;
		boolean isFirstPage = true;
		float colWidth = xPosition2 - 50 - margin;
		
		// Iterate through selected songs and populate the PDF
		for (Song song : selectedSongs) {
			
			contentStream.beginText();
			contentStream.setLeading(options.getLineSpacing());
			
			contentStream.newLineAtOffset(xPosition, yPosition);
			contentStream.setFont(fontBold, (float) (options.getFontSize()*1.05));
			
			if(yPosition - options.getLineSpacing()*4 < margin && column == 1) {
				xPosition = xPosition2;
				if(!isFirstPage) yPosition = yStart;
				else yPosition = yFirstPage;
				column = 2;
				contentStream.endText();
				contentStream.close();
				contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true);
				startText(contentStream, options.getLineSpacing(), xPosition, yPosition, fontBold, options.getFontSize());
				isFirstPage = false;
			} else if(yPosition - options.getLineSpacing()*4 < margin && column == 2) {
				contentStream.endText();
				contentStream.close();
				page = new PDPage(PDRectangle.A4);
				document.addPage(page);
				contentStream = new PDPageContentStream(document, page);
				yPosition = yStart;
				column = 1;
				xPosition = margin;
				startText(contentStream, options.getLineSpacing(), xPosition, yPosition, fontBold, options.getFontSize());
			}
			String songTitle = songNumber + ". " + song.getTitle();
			float songTitleWidth = fontBold.getStringWidth(songTitle) / 1000 * options.getFontSize();
			if(songTitleWidth > colWidth) {
				float totalWidth = 0;
				String[] songTitleArr = songTitle.split(" ");
				
				for(String word : songTitleArr) {
					float wordWidth = fontBold.getStringWidth(" " + word) / 1000 * options.getFontSize();
					totalWidth += wordWidth;
					if(totalWidth > colWidth) {
						contentStream.newLine(); // The error is here
						yPosition -= options.getLineSpacing();
						totalWidth = 0;
					}
					contentStream.showText(word + " ");
				}
				contentStream.newLine();
			} else {
				contentStream.showText(songTitle);
				contentStream.newLine();				
			}
			
			contentStream.setFont(fontItalic, (float) (options.getFontSize() * 0.9));
			contentStream.showText("Do = " + song.getKey());
			contentStream.newLine();
			contentStream.setFont(fontRegular, options.getFontSize());
			String lyrics = song.getLyrics();
			String[] textArr = lyrics.split("\n");
			yPosition = yPosition - (options.getLineSpacing() * 2);
			
			contentStream.endText();

			// add new page
			for (String text : textArr) {
				// Move to the second column
//				moveToNextColumnOrPage(contentStream, document, page, margin, column, xPosition, xPosition2, yPosition, isFirstPage, yStart, yFirstPage, fontRegular, options, (float) (options.getFontSize()));
				contentStream.beginText();
				contentStream.setLeading(options.getLineSpacing());
				contentStream.newLineAtOffset(xPosition, yPosition);
				contentStream.setFont(fontRegular, options.getFontSize());
				
				float addSpacing = 0;
				
				if(text.length() ==  9 || text.length() == 5) { // if(text == "Pra-reff:" || text == "Reff:")
					addSpacing = (float) 3 * (options.getLineSpacing());					
				}
				
				if (yPosition - options.getLineSpacing() - addSpacing < margin && column == 1) {
//					moveToColumn2(document, page, xPosition, xPosition2, yPosition, yStart, isFirstPage, yFirstPage, column, contentStream);
					
					xPosition = xPosition2;
					if(!isFirstPage) {
						yPosition = yStart;						
					} else {
						yPosition = yFirstPage;
					}
					column = 2;
					contentStream.endText();
					contentStream.close();
					contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true);
//					if(text == "") yPosition += options.getLineSpacing();
					startText(contentStream, options.getLineSpacing(), xPosition, yPosition, fontRegular, options.getFontSize());
					isFirstPage = false;
				} else if (yPosition - options.getLineSpacing() - addSpacing < margin && column == 2) {
					// Start a new page if the content exceeds the page height
//					moveToNextPage(contentStream, page, document, yPosition, yStart, column, xPosition, margin);
					
					contentStream.endText();
					contentStream.close();
					page = new PDPage(PDRectangle.A4);
					document.addPage(page);
					contentStream = new PDPageContentStream(document, page);
					yPosition = yStart;
					column = 1;
					xPosition = margin;
//					if(text == "") yPosition += options.getLineSpacing();
					startText(contentStream, options.getLineSpacing(), xPosition, yPosition, fontRegular, options.getFontSize());

				}
				
//				if(column == 1) rightMargin = xPosition2 - 50;
//				else if(column == 2) rightMargin = page.getMediaBox().getWidth() - margin;
//				
				float textWidth = fontRegular.getStringWidth(text.replace("\'", "‘").replace("\u2005", " ")) / 1000 * options.getFontSize();
				

				if(textWidth > colWidth) {
					float totalWidth = 0;
					String[] wordArr = text.split(" ");
					
					for(String word : wordArr) {
						float wordWidth = fontRegular.getStringWidth(" " + word) / 1000 * options.getFontSize();
						totalWidth += wordWidth;
						if(totalWidth > colWidth) {
							contentStream.newLine(); // The error is here
							yPosition -= options.getLineSpacing();
							totalWidth = 0;
						}
						contentStream.showText(word + " ");
					}
					contentStream.newLine();
				} else {
					// if it is an empty string and on the first line of the page or column, don't create spaces on the first line
					if((yPosition == yStart || yPosition == yFirstPage) && text.length() < 2) {
						yPosition += options.getLineSpacing();
					} else {
						contentStream.showText(text.replace("\'", "‘").replace("\u2005", " ")); // This is error handling,
						
					}
					// change this if error occurs
					contentStream.newLine();
					
					
				}
				yPosition -= options.getLineSpacing();
				
				contentStream.endText();
				
			}
			
			
			Optional<CustomCommentRequest> comment = comments.stream() 
															 .filter(item -> item.getSongId() == song.getId())
															 .findFirst();

			// Handle new line
			String fullComment = comment.get().getComment();
			if(fullComment != "") {
				if(yPosition - options.getFontSize()*0.9*1.2 < margin && column == 1) {
					xPosition = xPosition2;
					if(!isFirstPage) yPosition = yStart;
					else yPosition = yFirstPage;
					column = 2;
//					contentStream.endText();
					contentStream.close();
					contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true);
//					startText(contentStream, options.getLineSpacing(), xPosition, yPosition, fontBold, options.getFontSize());
					isFirstPage = false;
				} else if(yPosition - options.getFontSize()*0.9*1.2 < margin && column == 2) {
//					contentStream.endText();
					contentStream.close();
					page = new PDPage(PDRectangle.A4);
					document.addPage(page);
					contentStream = new PDPageContentStream(document, page);
					yPosition = yStart;
					column = 1;
					xPosition = margin;
//					startText(contentStream, options.getLineSpacing(), xPosition, yPosition, fontBold, options.getFontSize());
				}
				
				float yLinePosition = yPosition + options.getLineSpacing() - 12;
				contentStream.moveTo(xPosition, yLinePosition);
				contentStream.lineTo(xPosition+50, yLinePosition);
				contentStream.stroke();
				yPosition -= 10;
				
				
				String[] commentArr = fullComment.split("\n");
				
				// add new page
				for (String commentChunk : commentArr) {
					contentStream.beginText();
					contentStream.setLeading(options.getLineSpacing());
					contentStream.newLineAtOffset(xPosition, yPosition);
					contentStream.setFont(fontItalic, (float) (options.getFontSize()*0.9));
					
//					moveToNextColumnOrPage(contentStream, document, page, margin, column, xPosition2, xPosition2, yPosition, isFirstPage, yStart, yFirstPage, fontItalic, options, (float) (options.getFontSize()*0.9));
					
					// Move to the second column
					if (yPosition < margin && column == 1) {
//						moveToColumn2(document, page, xPosition, xPosition2, yPosition, yStart, isFirstPage, yFirstPage, column, contentStream);
						xPosition = xPosition2;
						if(!isFirstPage) yPosition = yStart;
						else yPosition = yFirstPage;
						isFirstPage = false;
						column = 2;
						contentStream.endText();
						contentStream.close();
						contentStream = new PDPageContentStream(document, page, AppendMode.APPEND, true);
						startText(contentStream, options.getLineSpacing(), xPosition, yPosition, fontItalic, (float) (options.getFontSize()*0.9));

					} else if (yPosition < margin && column == 2) {
						// Start a new page if the content exceeds the page height
//						moveToNextPage(contentStream, page, document, yPosition, yStart, column, xPosition, margin);
						
						contentStream.endText();
						contentStream.close();
						page = new PDPage(PDRectangle.A4);
						document.addPage(page);
						contentStream = new PDPageContentStream(document, page);
						yPosition = yStart;
						column = 1;
						xPosition = margin;
						startText(contentStream, options.getLineSpacing(), xPosition, yPosition, fontItalic, (float) (options.getFontSize()*0.9));
					}

//					startText(contentStream, options.getLineSpacing(), xPosition, yPosition, fontItalic, (float) (options.getFontSize()*0.9));
					
					contentStream.showText(commentChunk.replace("\'", "‘").replace("\u2005", " ")); // This is error
																									// handling, change this
																									// if error occurs
					contentStream.newLine();
					// Delete to undo
					contentStream.endText();
					
					if (commentChunk != "") {
						yPosition -= options.getLineSpacing();
					}
				}
			}
			
			songNumber++;
			yPosition -= 10;
		}
		contentStream.close();
		return document;
	}
	
	private void startText(PDPageContentStream stream, float lineSpacing, float x, float y, PDFont font, float fontSize) throws IOException {
		stream.beginText();
		stream.setLeading(lineSpacing);
		stream.newLineAtOffset(x, y);
		stream.setFont(font, fontSize);
	}

//	private PDPageContentStream moveToColumn2(PDDocument document, PDPage page, float x, float x2, float y, float yStart,
//					boolean isFirstPage, float yFirstPage, int column, PDPageContentStream stream) throws IOException {
//		x = x2;
//		if(!isFirstPage) y = yStart;
//		else y = yFirstPage;
//		isFirstPage = false;
//		column = 2;
//		stream.endText();
//		stream.close();
//		stream = new PDPageContentStream(document, page, AppendMode.APPEND, true);
//		return stream;
//	}
//	
//	private PDPageContentStream moveToNextPage(PDPageContentStream stream, PDPage page, PDDocument document, float y, float yStart, int column, float x, float margin) throws IOException {
//		stream.endText();
//		stream.close();
//		page = new PDPage(PDRectangle.A4);
//		document.addPage(page);
//		stream = new PDPageContentStream(document, page);
//		y = yStart;
//		column = 1;
//		x = margin;
//		return stream;
//	}
//	
//	private void moveToNextColumnOrPage(PDPageContentStream stream, PDDocument document, PDPage page, float margin, int column, float x, float x2, float y,
//									boolean isFirstPage, float yStart, float yFirstPage, PDFont font, PdfCustomizationOptions options, float fontSize) throws IOException {
//		if (y < margin && column == 1) {
//			x = x2;
//			if(!isFirstPage) y = yStart;
//			else y = yFirstPage;
//			isFirstPage = false;
//			column = 2;
//			stream.endText();
//			stream.close();
//			stream = new PDPageContentStream(document, page, AppendMode.APPEND, true);
//			startText(stream, options.getLineSpacing(), x, y, font, (float) fontSize);
//
//		} else if (y < margin && column == 2) {
//			// Start a new page if the content exceeds the page height
//			stream.endText();
//			stream.close();
//			page = new PDPage(PDRectangle.A4);
//			document.addPage(page);
//			stream = new PDPageContentStream(document, page);
//			y = yStart;
//			column = 1;
//			x = margin;
//			startText(stream, options.getLineSpacing(), x, y, font, (float) fontSize);
//		}
//	}
	
	private void splitText(int column, String text, float x, float y, float margin, float rightMargin, float x2) {
		if(column == 1) rightMargin = x2 - 50;
		else if(column == 2) rightMargin = margin;
	}
}
