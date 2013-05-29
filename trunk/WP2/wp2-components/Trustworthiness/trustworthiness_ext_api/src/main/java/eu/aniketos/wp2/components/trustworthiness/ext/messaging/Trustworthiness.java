package eu.aniketos.wp2.components.trustworthiness.ext.messaging;

import java.io.Serializable;

/**
 * 
 * @author Hisain Elshaafi
 *
 */

public class Trustworthiness implements Serializable {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 6908818728221634041L;
	protected String serviceId = null;
	protected double trustworthinessScore=0;
	protected double qosScore=0;
	protected double qosConfidence=0;
	protected double reputationScore=0;
	protected double reputationConfidence=0;
	protected double securityScore=0;
	
	/**
	 * 
	 */
	protected double averageComponentTrustworthinessScore = 0;

	/**
	 * 
	 */
	protected double lowestComponentTrustworthinessScore = 0;

	/**
	 * TODO: add status description e.g. unavailable info/experience
	 */

	/**
	 * @return
	 */
	
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param serviceId
	 */
	public void setServiceId(String id) {
		this.serviceId = id;
	}

	/**
	 * @return
	 */
	public double getTrustworthinessScore() {
		return trustworthinessScore;
	}

	/**
	 * @param trustworthinessScore
	 */
	public void setTrustworthinessScore(double trustworthinessScore) {
		this.trustworthinessScore = trustworthinessScore;
	}
	

	/**
	 * @return
	 */
	public double getQosScore() {
		return qosScore;
	}


	/**
	 * @param qosScore
	 */
	public void setQosScore(double qosScore) {
		this.qosScore = qosScore;
	}
	
	/**
	 * @return
	 */
	public double getQosConfidence() {
		return qosConfidence;
	}

	/**
	 * @param qosConfidence
	 */
	public void setQosConfidence(double confidence) {
		this.qosConfidence = confidence;
	}

	/**
	 * @return
	 */
	public double getSecurityScore() {
		return securityScore;
	}

	public void setSecurityScore(double securityScore) {
		this.securityScore = securityScore;
	}

	
	
	

	/**
	 * @return
	 */
	public double getReputationScore() {
		return reputationScore;
	}

	/**
	 * @param reputationScore
	 */
	public void setReputationScore(double reputationScore) {
		this.reputationScore = reputationScore;
	}

	/**
	 * @return
	 */
	public double getReputationConfidence() {
		return reputationConfidence;
	}

	/**
	 * @param reputationConfidence
	 */
	public void setReputationConfidence(double reputationConfidence) {
		this.reputationConfidence = reputationConfidence;
	}

	

	
	/**
	 * @return
	 */
	public double getAverageComponentTrustworthinessScore() {

		return averageComponentTrustworthinessScore;
	}

	/**
	 * @param trustworthinessScore
	 */
	public void setAverageComponentTrustworthinessScore(
			double trustworthinessScore) {
		this.averageComponentTrustworthinessScore = trustworthinessScore;

	}


	/**
	 * @return
	 */
	public double getLowestComponentTrustworthinessScore() {
		return lowestComponentTrustworthinessScore;
	}

	
	/**
	 * @param trustworthinessScore
	 */
	public void setLowestComponentTrustworthinessScore(
			double trustworthinessScore) {
		this.lowestComponentTrustworthinessScore = trustworthinessScore;
	}

	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Trustworthiness other = (Trustworthiness) obj;
		if (serviceId == null) {
			if (other.serviceId != null)
				return false;
		} else if (!serviceId.equals(other.serviceId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Service [serviceId=" + serviceId + " -> trustworthiness=" + trustworthinessScore +"]";
	}

}
