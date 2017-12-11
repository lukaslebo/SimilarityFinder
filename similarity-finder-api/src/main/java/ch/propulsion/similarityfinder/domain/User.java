package ch.propulsion.similarityfinder.domain;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="users")
@Data
@EqualsAndHashCode(exclude="id")
@NoArgsConstructor
public class User {
	
	private static Long accountExpirationPeriodInMinutes = 180L;
	
	@Id
	private String id;
	
	@Column(name="expiration_date", nullable=false)
	private LocalDateTime expirationDate;
	
	@OneToOne
	private Document document = null;
	
	@OneToMany
	@Setter(value=AccessLevel.PRIVATE)
	private Map<String, Document> resources = new HashMap<>();
	
	@ElementCollection(fetch=FetchType.EAGER)
	@Setter(value=AccessLevel.PRIVATE)
	private Set<String> resourceIds = new HashSet<>();
	
	@OneToMany
	private List<Similarity> similarities = new ArrayList<>();
		
	@PrePersist
	public void onCreate() {
		String uuid = UUID.randomUUID().toString();			
		setId(uuid);
		if (expirationDate == null) {
			setExpirationDate(LocalDateTime.now(Clock.systemUTC()).plusMinutes(accountExpirationPeriodInMinutes));
		}
	}
	
	public void addResource(String resourceId, Document resource) {
		this.resourceIds.add(resourceId);
		this.resources.put(resourceId, resource);
	}
	
	public void removeResource(String resourceId) {
		this.similarities = this.similarities.stream()//
				.filter(sim -> !sim.getResourceId().equals(resourceId))//
				.collect(Collectors.toList());
		this.resourceIds.remove(resourceId);
		this.resources.remove(resourceId);
	}
	
	public void removeDocument() {
		this.similarities = new ArrayList<>();
		this.document = null;
	}
	
	public void addSimilarity(Similarity similarity) {
		this.similarities.add(similarity);
	}
	
	public void refreshExpirationDate() {
		this.expirationDate = LocalDateTime.now(Clock.systemUTC()).plusMinutes(accountExpirationPeriodInMinutes);
	}
	
}
