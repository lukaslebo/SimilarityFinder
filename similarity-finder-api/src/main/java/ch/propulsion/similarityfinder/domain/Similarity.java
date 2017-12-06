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
	
	@Column(nullable=false)
	private Double similarity;
	
	private boolean isUnique = true;
	
	public Similarity(String id, Integer startIndex, Integer endIndex, String resourceId, Integer resourceStartIndex,
			Integer resourceEndIndex, Double similarity) {
		this.id = id;
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.resourceId = resourceId;
		this.resourceStartIndex = resourceStartIndex;
		this.resourceEndIndex = resourceEndIndex;
		this.similarity = similarity;
	}
	
	public Similarity(Integer startIndex, Integer endIndex, String resourceId, Integer resourceStartIndex,
			Integer resourceEndIndex, Double similarity) {
		this(null, startIndex, endIndex, resourceId, resourceStartIndex, resourceEndIndex, similarity);
	}
	
	@PrePersist
	public void onCreate() {
		String uuid = UUID.randomUUID().toString();
		setId(uuid);
	}
	
	public int size() {
		return endIndex-startIndex;
	}
	
	public boolean isContainedIn(Similarity sim) {	
		if (
			this.startIndex >= sim.startIndex && 
			this.endIndex <= sim.endIndex
		) {
			return true;
		}
		return false;
	}
	
	public boolean isOverlappingWith(Similarity sim) {
		if (
			(this.startIndex >= sim.startIndex &&
			this.startIndex <= sim.endIndex) ||
			(this.endIndex >= sim.startIndex &&
			this.endIndex <= sim.endIndex)
		) {
			return true;
		}
		return false;
	}
	
}
