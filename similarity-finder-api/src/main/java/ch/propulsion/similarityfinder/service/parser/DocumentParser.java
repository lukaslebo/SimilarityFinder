package ch.propulsion.similarityfinder.service.parser;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import ch.propulsion.similarityfinder.domain.Document;

public interface DocumentParser {
	
	Document parseFileToDocument(Path path) throws Exception;
	List<Document> parseFilesToDocument(Stream<Path> paths) throws Exception;
	
}
