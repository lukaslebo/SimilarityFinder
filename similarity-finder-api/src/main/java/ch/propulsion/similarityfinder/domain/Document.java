package ch.propulsion.similarityfinder.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="documents")
@Data
@EqualsAndHashCode(exclude="id")
@NoArgsConstructor
public class Document {
	
	@Id
	private String id;
	
	@Column(name="file_name", nullable=false, length=255)
	private String fileName;
	
	@Column(name="date_created")
	private LocalDateTime dateCreated = LocalDateTime.now();
	
	@Lob
	@Column(columnDefinition="CLOB", nullable=false)
	private String content;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private List<String> parsedDocument_punctuated = new ArrayList<>();
	
	@ElementCollection
	private List<String> parsedDocument = new ArrayList<>();
	
	@ElementCollection
	private List<Integer> sentenceStartIndex = new ArrayList<>();
	
	@ElementCollection
	private List<Integer> sentenceEndIndex = new ArrayList<>();
	
	@Column(name="is_parsed", nullable=false)
	private boolean isParsed = false;
	
	public Document(String id, String content, String fileName) {
		this.id = id;
		this.content = content;
		this.fileName = fileName;
	}
	
	public Document(String content, String fileName) {
		this(null, content, fileName);
	}
	
	@PrePersist
	public void onCreate() {
		String uuid = UUID.randomUUID().toString();			
		setId(uuid);
	}
	
	public void setContent(String content) {
		this.content = content;
		this.isParsed = false;
	}
	
	public void parse() {
		if (isParsed || content == null || content.length() == 0) {
			return;
		}
		content = content.replaceAll("[\\s&&[^\\n\\r]]+", " ");
		content = content.trim();
		parsedDocument_punctuated = Arrays.asList(content.replaceAll("[\\s]+", " ").split("\\s"));
		parsedDocument = Arrays.asList(content.replaceAll("\\p{Punct}", "").split("\\s"));
		if (parsedDocument.size() == 0) {
			return;
		}
		if (sentenceStartIndex.size() > 0 || this.sentenceEndIndex.size() > 0) {
			sentenceStartIndex = new ArrayList<>();
			sentenceEndIndex = new ArrayList<>();
		}
		sentenceStartIndex.add(0);
		int docSize = parsedDocument_punctuated.size();
		for (int i = 0; i < docSize; i++) {
			String word = parsedDocument_punctuated.get(i);
			if (word.length() > 0 && (word.charAt(word.length()-1)+"").matches("[.?!]")) {
				sentenceEndIndex.add(i);
				if (i < docSize-1) {
					sentenceStartIndex.add(i+1);
				}
			}
		}
		if (sentenceStartIndex.size() > sentenceEndIndex.size()) {
			sentenceEndIndex.add(docSize-1);
		}
		isParsed = true;
	}
	
	public String getSubset(int start, int end) {
		if (end < start || start < 0 || end > parsedDocument_punctuated.size()-1) {
			throw new IllegalArgumentException("Start index must come before end index and both must be within the range of the document.");
		}
		return String.join(" ", parsedDocument_punctuated.subList(start, end+1));
	}
	
}
