package ch.propulsion.similarityfinder.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name="similarities")
@Data
@EqualsAndHashCode(exclude="id")
@NoArgsConstructor
public class Similarity {
	
	@Id
	private String id;
	
	@Column(name="start_index", nullable=false)
	private Integer startIndex;
	
	@Column(name="end_index", nullable=false)
	private Integer endIndex;
	
	@Column(name="resource_id", nullable=false)
	private String resourceId;
	
	@Column(name="resource_start_index", nullable=false)
	private Integer resourceStartIndex;
	
	@Column(name="resource_end_index", nullable=false)
	private Integer resourceEndIndex;
	
	@Column(name="severity_rating", nullable=false)
	private Integer severityRating;
	
	public Similarity(String id, Integer startIndex, Integer endIndex, String resourceId, Integer resourceStartIndex,
			Integer resourceEndIndex, Integer severityRating) {
		this.id = id;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.resourceId = resourceId;
		this.resourceStartIndex = resourceStartIndex;
		this.resourceEndIndex = resourceEndIndex;
		this.severityRating = severityRating;
	}
	
	public Similarity(Integer startIndex, Integer endIndex, String resourceId, Integer resourceStartIndex,
			Integer resourceEndIndex, Integer severityRating) {
		this(null, startIndex, endIndex, resourceId, resourceStartIndex, resourceEndIndex, severityRating);
	}
	
	@PrePersist
	public void onCreate() {
		String uuid = UUID.randomUUID().toString();
		setId(uuid);
	}
	
}
