package ch.propulsion.similarityfinder.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
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
	
	@Column(name="date_created")
	private LocalDateTime dateCreated = LocalDateTime.now();
	
	@Lob
	@Column(columnDefinition="CLOB", nullable=false)
	private String content;
	
	@ElementCollection
	private List<String> parsedDocument_punctuated = new ArrayList<>();
	
	@ElementCollection
	private List<String> parsedDocument = new ArrayList<>();
	
	@ElementCollection
	private List<Integer> sentenceStartIndex = new ArrayList<>();
	
	@ElementCollection
	private List<Integer> sentenceEndIndex = new ArrayList<>();
	
	@Column(name="is_parsed", nullable=false)
	private boolean isParsed = false;
	
	public Document(String id, String content) {
		this.id = id;
		this.content = content;
	}
	
	public Document(String content) {
		this(null, content);
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
		if (this.isParsed || this.content == null || this.content.length() == 0) {
			return;
		}
		this.parsedDocument_punctuated = Arrays.asList(this.content.split("\\s"));
		this.parsedDocument = Arrays.asList(this.content.replaceAll("\\p{Punct}", "").split("\\s"));
		if (this.parsedDocument.size() == 0) {
			return;
		}
		if (this.sentenceStartIndex.size() > 0 || this.sentenceEndIndex.size() > 0) {
			this.sentenceStartIndex = new ArrayList<>();
			this.sentenceEndIndex = new ArrayList<>();
		}
		this.sentenceStartIndex.add(0);
		int docSize = this.parsedDocument_punctuated.size();
		for (int i = 0; i < docSize; i++) {
			String word = parsedDocument_punctuated.get(i);
			if ((word.charAt(word.length()-1)+"").matches("[.?!]")) {
				this.sentenceEndIndex.add(i);
				if (i < docSize-1) {
					this.sentenceStartIndex.add(i+1);
				}
			}
		}
		if (this.sentenceStartIndex.size() > this.sentenceEndIndex.size()) {
			this.sentenceEndIndex.add(docSize-1);
		}
		this.isParsed = true;
	}
	
}
