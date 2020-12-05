package app.treperday.api.domain.performance;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Attachment {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne
	@JoinColumn(name="performance_id")
	@JsonManagedReference
	private Performance performance;
	
	private String uri;
	
	private String type;
	
	private String fetchToken;
	
	public Attachment(Performance performance, String uri, String type, String fetchToken) {
		this.performance = performance;
		this.uri = uri;
		this.type = type;
		this.fetchToken = fetchToken;
	}

	public Attachment() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Performance getPerformance() {
		return performance;
	}

	public void setPerformance(Performance performance) {
		this.performance = performance;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFetchToken() {
		return fetchToken;
	}

	public void setFetchToken(String fetchToken) {
		this.fetchToken = fetchToken;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fetchToken == null) ? 0 : fetchToken.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Attachment))
			return false;
		Attachment other = (Attachment) obj;
		if (fetchToken == null) {
			if (other.fetchToken != null)
				return false;
		} else if (!fetchToken.equals(other.fetchToken))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Attachment [id=" + id + ", uri=" + uri + ", type=" + type + ", fetchToken=" + fetchToken + "]";
	}
	
}
