package eu.aniketos.wp2.components.trustworthiness.rules.service;

import java.util.Map;

import eu.aniketos.wp2.components.trustworthiness.ext.rules.model.event.TrustEvent;

/**
 * @author Hisain Elshaafi (TSSG)
 * triggers generation of ratings through invoking JBoss rules
 */
public interface MetricRatingUpdate {

	/**
	 * @param metric
	 * @throws Exception
	 */
	void updateScore(Map<String, String> metric) throws Exception;

	/**
	 * @param event
	 * @throws Exception
	 */
	void updateScore(TrustEvent event) throws Exception;

}