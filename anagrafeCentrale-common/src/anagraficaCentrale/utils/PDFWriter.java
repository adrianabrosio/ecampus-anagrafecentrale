package anagraficaCentrale.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 * this is a class to write a pdf file. It uses the Apache Pdfbox libs
 */
public class PDFWriter {
	final static Logger logger = LogManager.getRootLogger();
	
	private String pdfOutputDirectory = "";
	private String pdfFileName = "";
	private PDDocument doc = null;
	private PDFont font = null;

	public PDFWriter(String pdfOutputDirectory, String pdfFileName) {
		this.pdfOutputDirectory = pdfOutputDirectory;
		if (!this.pdfOutputDirectory.endsWith("/"))
			this.pdfOutputDirectory += "/";
		if (!pdfFileName.endsWith(".pdf")) {
			pdfFileName = pdfFileName + ".pdf";
		}
		this.pdfFileName = pdfFileName;
	}

	public PDFWriter(String pdfOutputFullPathName) {
		int index = pdfOutputFullPathName.lastIndexOf("/");
		int index2 = pdfOutputFullPathName.lastIndexOf("\\");
		if(index2 > index)
			index = index2;
		if(index == -1){
			throw new IllegalArgumentException("Invalid path: " + pdfOutputFullPathName);
		}
		this.pdfOutputDirectory = pdfOutputFullPathName.substring(0, index+1);

		if (!pdfOutputFullPathName.endsWith(".pdf")) {
			pdfOutputFullPathName = pdfOutputFullPathName + ".pdf";
		}
		this.pdfFileName = pdfOutputFullPathName.substring(index+1, pdfOutputFullPathName.length());
	}

	public void createPdfFile() {
		doc = new PDDocument();
		try {
			font = PDType0Font.load(doc, new File("/Windows/Fonts" +"/BKANT.TTF"));
		} catch (IOException e) {
			font = PDType1Font.HELVETICA;
			e.printStackTrace();
		}
	}

	public boolean addPage(String pageHeader, StringBuffer pageText, String imageDirectory, List<BufferedImage> list) {
		boolean ok = false;
		//Create and add the page to the document
		PDPage page = new PDPage();
		doc.addPage(page);
		PDPageContentStream contents = null;

		float fontSize = 12;
		float leading = 1.5f*fontSize;
		PDRectangle mediabox = page.getMediaBox();
		float margin = 75;
		float width = mediabox.getWidth() - 2*margin;
		float startX = mediabox.getLowerLeftX() + margin;
		float startY = mediabox.getUpperRightY() - margin;
		float yOffset = startY;

		try {
			contents = new PDPageContentStream(doc, page);
			contents.beginText();
			contents.setFont(font, 14);
			contents.newLineAtOffset(startX, startY);
			yOffset-=leading;
			contents.showText(pageHeader);
			contents.newLineAtOffset(0, -leading);
			yOffset-=leading;

			List<String> lines = new ArrayList<>();
			parseIndividualLines(pageText, lines, fontSize, font, width);

			contents.setFont(font, fontSize);
			for (String line:lines) { 
				contents.showText(line);
				contents.newLineAtOffset(0, -leading);
				yOffset-=leading;

				if (yOffset <= 0) {
					contents.endText();
					try {
						if (contents != null) contents.close();
					} catch (IOException e) {
						ok = false;
						e.printStackTrace();
					}
					page = new PDPage();
					doc.addPage(page);
					contents = new PDPageContentStream(doc, page);
					contents.beginText();
					contents.setFont(font, fontSize);
					yOffset = startY;
					contents.newLineAtOffset(startX, startY);
				}
			}
			contents.endText();

			if(list != null){
				float scale = 1f;
				for (BufferedImage attachment : list) {
					logger.debug("Adding image: " + imageDirectory + attachment);
					Path temp = Files.createTempFile("tmp_image", ".png");
					File tmpFile = temp.toFile();
					ImageIO.write(attachment, "png", tmpFile);
					PDImageXObject pdImage = PDImageXObject.createFromFileByContent(tmpFile, doc);
					//scale = width/pdImage.getWidth();
					yOffset-=(pdImage.getHeight()*scale);
					if (yOffset <= 0) {
						logger.debug("Starting a new page");
						try {
							if (contents != null) contents.close();
						} catch (IOException e) {
							ok = false;
							e.printStackTrace();
						}
						page = new PDPage();
						doc.addPage(page);
						contents = new PDPageContentStream(doc, page);
						yOffset = startY-(pdImage.getHeight()*scale);
					}
					logger.debug("yOffset: " + yOffset);
					logger.debug("page width: " + width + "  imageWidth: " + pdImage.getWidth() + " imageHeight: " + (pdImage.getHeight()*scale) + " scale: " + scale);
					contents.drawImage(pdImage, startX, yOffset, width, pdImage.getHeight()*scale);
				}
			}
			ok = true;
		} catch (IOException e) {
			logger.error(e);
			ok = false;
		} finally {
			try {
				if (contents != null) contents.close();
			} catch (IOException e) {
				ok = false;
				logger.error(e);
			}
		}

		return ok;
	}

	public String saveAndClose() {
		try {
			doc.save(pdfOutputDirectory + pdfFileName);
			return pdfOutputDirectory + pdfFileName;
		} catch (IOException e) {
			logger.error(e);
		} finally {
			try {
				doc.close();
			} catch (IOException e) {
				logger.error(e);
			}
		}
		return null;
	}

	private void parseIndividualLines(StringBuffer wholeLetter, List<String> lines, float fontSize, PDFont pdfFont, float width) throws IOException {
		logger.debug("PDFWRITER: file content to be parsed: ["+wholeLetter.toString()+"]");
		String[] paragraphs = wholeLetter.toString().split("\n");
		logger.debug("PDFWRITER: total paragraphs found: "+paragraphs.length);
		for (int i = 0; i < paragraphs.length; i++) {
			int lastSpace = -1;
			lines.add(" ");
			while (paragraphs[i].length() > 0) {
				int spaceIndex = paragraphs[i].indexOf(' ', lastSpace + 1);
				if (spaceIndex < 0) {
					spaceIndex = paragraphs[i].length();
				}
				String subString = paragraphs[i].substring(0, spaceIndex);
				float size = fontSize * pdfFont.getStringWidth(subString) / 1000;
				//System.out.printf("'%s' - %f of %f\n", subString, size, width);
				if (size > width) {
					if (lastSpace < 0) {
						lastSpace = spaceIndex;
					}
					subString = paragraphs[i].substring(0, lastSpace);
					lines.add(subString);
					paragraphs[i] = paragraphs[i].substring(lastSpace).trim();
					//System.out.printf("'%s' is line\n", subString);
					lastSpace = -1;
				} else if (spaceIndex == paragraphs[i].length()) {
					lines.add(paragraphs[i]);
					//System.out.printf("'%s' is line\n", paragraphs[i]);
					paragraphs[i] = "";
				} else {
					lastSpace = spaceIndex;
				}
			}
		}
	}
}
