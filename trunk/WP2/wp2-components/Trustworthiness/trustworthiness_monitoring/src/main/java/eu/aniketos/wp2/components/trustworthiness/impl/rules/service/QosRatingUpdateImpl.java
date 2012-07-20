package eu.aniketos.wp2.components.trustworthiness.impl.rules.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;

import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import org.apache.log4j.Logger;
import eu.aniketos.wp2.components.trustworthiness.configuration.ConfigurationManagement;
import eu.aniketos.wp2.components.trustworthiness.impl.rules.model.event.AlertEventImpl;
import eu.aniketos.wp2.components.trustworthiness.impl.rules.model.event.MetricEventImpl;
import eu.aniketos.wp2.components.trustworthiness.rules.service.RuleExecuter;
import eu.aniketos.wp2.components.trustworthiness.rules.service.RatingUpdate;
import eu.aniketos.wp2.components.trustworthiness.trust.management.TrustFactory;
import eu.aniketos.wp2.components.trustworthiness.trust.service.RatingEntityService;
import eu.aniketos.wp2.components.trustworthiness.trust.service.ServiceEntityService;
import eu.aniketos.wp2.components.trustworthiness.impl.trust.pojo.Atomic;
import eu.aniketos.wp2.components.trustworthiness.impl.trust.pojo.Rating;

/**
 * @author Hisain Elshaafi (TSSG)
 * 
 */
public class QosRatingUpdateImpl extends Observable implements RatingUpdate {

	private static Logger logger = Logger.getLogger(QosRatingUpdateImpl.class);

	private ConfigurationManagement config;

	private ServiceEntityService serviceEntityService;

	private RatingEntityService ratingEntityService;

	private TrustFactory trustFactory;

	private RuleExecuter ruleExecuter;

	private EventAdmin eventAdmin;

	/**
	 * 
	 */
	public void initialize() {
		// replaced with osgi events *whiteboard*
		// monitorHelperService.setupObservers(this);

		/*
		 * Map<String,Rating> props = new HashMap<String,Rating>();
		 * props.put("test", new Rating()); logger.info(eventAdmin);
		 * eventAdmin.sendEvent(new Event("trust-event/qos", props));
		 */
	}

	/* (non-Javadoc)
	 * @see eu.aniketos.wp2.components.trustworthiness.rules.service.RatingUpdate#updateScore(java.util.Map)
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void updateScore(Map<String, String> event) throws Exception {

		String serviceId = event.get("serviceId");
		Atomic service = null;
		if ((service = serviceEntityService.getAtomic(serviceId)) == null) {
			logger.info("creating new service entry");
			service = trustFactory.createService(serviceId);

			serviceEntityService.addAtomic(service);
		}

		if (!event.containsKey("property")
				|| (event.containsKey("subproperty") && event
						.get("subproperty") == null)
				|| !event.containsKey("type") || event.get("property") == null
				|| event.get("type") == null) {
			
			logger.warn("metric did not contain required event elements.");
			logger.warn("message will be ignored.");
			throw new Exception(
					"metric did not contain required event elements. message will be ignored.");

		}

		List<Object> facts = new ArrayList<Object>();

		/*
		 * TODO: organise with type of metric, content, names, etc
		 */

		String property = event.get("property");
		String propertySub = property;
		String subproperty = null;
		if (event.containsKey("subproperty")) {
			subproperty = event.get("subproperty");
			propertySub = property + "." + subproperty;
			if (logger.isDebugEnabled()) {
				logger.debug(property);
			}
		}

		String metricValue = event.get("value");
		
		if (metricValue==null){
			throw new Exception(
					"metric did not contain required event elements. message will be ignored.");
		}

		String contractValue = config.getConfig().getString(propertySub + "[@value]");
		String type = config.getConfig().getString(propertySub + "[@type]");
		String limit = config.getConfig().getString(propertySub + "[@limit]");

		if (logger.isDebugEnabled()) {
			logger.debug("property=" + propertySub + ", contractValue=" + contractValue
					+ ", type=" + type + ", limit=" + limit);
		}

		// if property is missing quit
		if (contractValue==null || type==null || limit==null){
			logger.warn("property " + property + " for the metric is missing in configuration.");
			return;
		}
		
		if (event.get("type").equalsIgnoreCase("metric")) {

			facts.add(new MetricEventImpl(service, property, subproperty,
					contractValue, type, limit, metricValue));
		} else if (event.get("type").equalsIgnoreCase("alert")) {
			facts.add(new AlertEventImpl(service, property, subproperty, contractValue,
					type, limit, String.valueOf(1)));
		}

		Rating rating = trustFactory.createRating(service);

		Map scoreMap = new HashMap();
		scoreMap.put("_type_", "Score");
		scoreMap.put("service", serviceId);
		facts.add(scoreMap);

		logger.debug("now firing rules for score update");

		Collection<?> results = ruleExecuter.execute(facts,
				property.toLowerCase(), "Score", "score");
		Iterator<?> scoreIterator = results.iterator();
		
		if (scoreIterator.hasNext()) {
			scoreMap = (Map) scoreIterator.next();

			logger.debug(scoreMap.entrySet());
		} else {
			logger.warn("no score retrieved from rule");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("size of retrieved score map " + scoreMap.size());
		}

		if (scoreMap.containsKey("score")) {
			Double scoreValue = (Double) scoreMap.get("score");
			BigDecimal scoreBD = new BigDecimal(String.valueOf(scoreValue)).setScale(6, BigDecimal.ROUND_HALF_UP);

			scoreValue = Double.parseDouble(scoreBD.toString());
			
			rating.setScore(scoreValue);
			rating.setRecency((Long) scoreMap.get("recency"));
			rating.setProperty((String) scoreMap.get("property"));

			ratingEntityService.addRating(rating);

			Dictionary props = new Properties();
			props.put("service.id",serviceId);
			props.put("score.id", rating.getId());
			
			Event osgiEvent = new Event("eu/aniketos/trustworthiness/qos", props);
			eventAdmin.sendEvent(osgiEvent);
			
			logger.debug("sent event to topic eu/aniketos/trustworthiness/qos ");

		} else {
			logger.warn("no score calculated from alert for " + service.getId());
		}
	}

	// needs testing
	/**
	 * required for Spring dependency injection
	 * 
	 * @param o
	 */
	public void addRemoteObserver(Observer o) {
		addObserver(o);

	}

	/**
	 * required for Spring dependency injection
	 * 
	 * @return
	 */
	public ConfigurationManagement getConfig() {
		return config;
	}

	/**
	 * @param config
	 */
	public void setConfig(ConfigurationManagement config) {
		this.config = config;
	}

	/**
	 * required for Spring dependency injection
	 * 
	 * @return
	 */
	public ServiceEntityService getServiceEntityService() {
		return serviceEntityService;
	}

	/**
	 * required for Spring dependency injection
	 * 
	 * @param serviceEntityService
	 */
	public void setServiceEntityService(
			ServiceEntityService serviceEntityService) {
		this.serviceEntityService = serviceEntityService;
	}

	/**
	 * required for Spring dependency injection
	 * 
	 * @return
	 */
	public RatingEntityService getScoreEntityService() {
		return ratingEntityService;
	}

	/**
	 * required for Spring dependency injection
	 * 
	 * @param ratingEntityService
	 */
	public void setScoreEntityService(RatingEntityService ratingEntityService) {
		this.ratingEntityService = ratingEntityService;
	}

	/**
	 * required for Spring dependency injection
	 * 
	 * @return
	 */
	public TrustFactory getTrustFactory() {
		return trustFactory;
	}

	/**
	 * required for Spring dependency injection
	 * 
	 * @param trustFactory
	 */
	public void setTrustFactory(TrustFactory trustFactory) {
		this.trustFactory = trustFactory;
	}

	/**
	 * required for Spring dependency injection
	 * 
	 * @return
	 */
	public RuleExecuter getRuleExecuter() {
		return ruleExecuter;
	}

	/**
	 * required for Spring dependency injection
	 * 
	 * @param ruleExecuter
	 */
	public void setRuleExecuter(RuleExecuter ruleExecuter) {
		this.ruleExecuter = ruleExecuter;
	}

	/**
	 * required for Spring dependency injection
	 * 
	 * @return OSGi event admin
	 */
	public EventAdmin getEventAdmin() {
		return eventAdmin;
	}
	
	/**
	 * required for Spring dependency injection
	 * 
	 * @param eventAdmin OSGi event admin
	 */
	public void setEventAdmin(EventAdmin eventAdmin) {
		this.eventAdmin = eventAdmin;
	}
}