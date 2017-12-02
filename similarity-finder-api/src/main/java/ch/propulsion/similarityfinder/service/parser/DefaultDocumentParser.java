package ch.propulsion.similarityfinder.service.parser;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import ch.propulsion.similarityfinder.domain.Document;

@Service
public class DefaultDocumentParser implements DocumentParser {

	@Override
	public Document parseFileToDocument(Path path) throws Exception {
		Document document = null;
		byte[] data = Files.readAllBytes(path);
		File file = path.toFile();
		if (is_pdf(data) && file.getName().endsWith(".pdf")) {
			document = parsePDF(file);
		}
		else if (file.getName().endsWith(".txt")) {
			document = parseTXT(file);
		}
		return document;
	}

	@Override
	public List<Document> parseFilesToDocument(Stream<Path> paths) throws Exception {
		List<Document> documents = new ArrayList<>();
		paths.forEach(path -> {
			try {
				documents.add(parseFileToDocument(path));				
			} catch (Exception e) {
				System.err.println(e);
				e.printStackTrace(System.out);
			}
		});
		return documents;
	}
	
	private static boolean is_pdf(byte[] data) {
	    if (data != null && data.length > 4 &&
	            data[0] == 0x25 && // %
	            data[1] == 0x50 && // P
	            data[2] == 0x44 && // D
	            data[3] == 0x46 && // F
	            data[4] == 0x2D) { // -
	    		return true;
	    }
	    return false;
	}
	
	private static Document parsePDF(File file) throws Exception {
		PDDocument pdfDoc = PDDocument.load(file);
		PDFTextStripper pdfStripper = new PDFTextStripper();
		String fileContent = pdfStripper.getText(pdfDoc);
		pdfDoc.close();
		return new Document(fileContent, file.getName());
	}
	
	private static Document parseTXT(File file) throws Exception {
		FileInputStream inputStream = new FileInputStream(file);     
		String fileContent = IOUtils.toString(inputStream, Charset.defaultCharset());
		inputStream.close();
		return new Document(fileContent, file.getName());
	}

}
