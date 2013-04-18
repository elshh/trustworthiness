package eu.aniketos.wp2.components.trustworthiness.trust.management.atomic;

import eu.aniketos.wp2.components.trustworthiness.impl.trust.pojo.Rating;
import eu.aniketos.wp2.components.trustworthiness.impl.trust.pojo.Trustworthiness;


/**
 * @author Hisain Elshaafi (TSSG)
 *
 */
public interface ServiceTrustUpdate {

	/**
	 * @param rating a rating score
	 * @param policy a configuration of trustworthiness procedure
	 * @return trustworthiness object
	 * @throws Exception
	 */
	public abstract Trustworthiness calculateScore(Rating rating, ServiceTrustUpdatePolicy policy) throws Exception;

}