/**
 *
 */
package eu.aniketos.wp2.components.trustworthiness.impl.trust.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

//import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author Hisain Elshaafi (TSSG)
 * 
 */
@Entity
@org.hibernate.annotations.Proxy(lazy = false)
@Table(name = "THREAT_LEVEL")
public class ThreatLevel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2106390902248255849L;

	private String id;

	private Service service;

	private double threatLevel;

	// @Length(max = 25)
	private String threatName;

	// @Length(max = 50)
	private long recency;

	private String updateDescription;

	/**
	 *
	 */
	public ThreatLevel() {
	}

	/**
	 * @param id
	 * @param serviceId
	 * @param threatLevel
	 * @param recency
	 * @param threatName
	 */
	public ThreatLevel(String id, Service service, double level, long recency,
			String threat) {
		this.id = id;
		this.service = service;
		this.threatLevel = level;
		this.threatName = threat;
	}

	/**
	 * @return
	 */
	@Id
	@Column(name = "id")
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return
	 */
	@NotNull
	@ManyToOne
	// (cascade = { CascadeType.ALL })
	@JoinColumn(name = "service_id")
	public Service getService() {
		return service;
	}

	/**
	 * @param serviceId
	 */
	public void setService(Service service) {
		this.service = service;
	}

	/**
	 * @return
	 */
	@NotNull
	@Column(precision = 6, scale = 2)
	public double getThreatLevel() {
		return threatLevel;
	}

	/**
	 * @param threatLevel
	 */
	public void setThreatLevel(double level) {
		this.threatLevel = level;
	}

	/**
	 * @return
	 */
	@NotNull
	public String getThreatName() {
		return threatName;
	}

	/**
	 * @param threatName
	 */
	public void setThreatName(String property) {
		this.threatName = property;
	}

	/**
	 * @return
	 */
	@NotNull
	public long getRecency() {
		return recency;
	}

	/**
	 * @param recency
	 */
	public void setRecency(long recency) {
		this.recency = recency;
	}

	/**
	 * @return updateDescription description of latest threatName update
	 */
	@Column(name="update_description")
	public String getUpdateDescription() {
		return updateDescription;
	}

	/**
	 * @param updateDescription
	 *            description of latest security threatName update
	 */
	public void setUpdateDescription(String updateDescription) {
		this.updateDescription = updateDescription;
	}

	@Override
	public boolean equals(Object o) {

		if (this == o)
			return true;
		if (o == null || !(o instanceof ThreatLevel)) {

			return false;
		}

		ThreatLevel other = (ThreatLevel) o;

		/*
		 * equivalence by id
		 */
		ThreatLevel castOther = (ThreatLevel) other;
		return new EqualsBuilder().append(id, castOther.getId())
				.append(service, castOther.getService())
				.append(threatName, castOther.getThreatName()).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(441293447, 2056268651).append(id)
				.append(service).append(threatLevel).append(threatName).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", id)
				.append("serviceId", service).append("threatLevel", threatLevel)
				.append("threatName", threatName).toString();
	}
}
