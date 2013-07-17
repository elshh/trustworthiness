package eu.aniketos.trustworthiness.alert;

import org.apache.log4j.Logger;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import eu.aniketos.trustworthiness.trust.service.ServiceEntityService;
import eu.aniketos.wp4.components.notification.IAlert;
import eu.aniketos.wp4.components.notification.Notification;

public class TrustworthinessChangeAlerter implements EventHandler {

	private static Logger logger = Logger
			.getLogger(TrustworthinessChangeAlerter.class);

	private ServiceEntityService serviceEntityService;

	private IAlert alert;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.service.event.EventHandler#handleEvent(org.osgi.service.event
	 * .Event)
	 */
	public void handleEvent(Event event) {

		String topicName = event.getTopic();

		if (topicName.endsWith("eu/aniketos/trustworthiness/prediction/alert")) {

			if (logger.isDebugEnabled()) {
				logger.debug("Received a new trust alert, type:" + event.getProperty("alert.type"));
				logger.debug("with properties:");
				String[] propertyNames = event.getPropertyNames();
				for (String propertyName : propertyNames) {
					String propertyValue = (String) event
							.getProperty(propertyName);
					logger.debug("- " + propertyName + ": " + propertyValue);
				}
			}

			String serviceId = (String) event.getProperty("service.id");
			String trustScore = (String) event
					.getProperty("trustworthiness.score");
			String alertDescription = (String) event.getProperty("alert.description");

			if (event.getProperty("alert.type").equals("TRUST_LEVEL_CHANGE")) {
				alert.alert(serviceId, Notification.TRUST_LEVEL_CHANGE, trustScore, alertDescription);
			}

		} else {
			if (logger.isInfoEnabled()) {
				logger.info("Received event is not an alert, topic:"
						+ topicName);
			}
		}

	}

	/**
	 * @return data access service object for Web service
	 */
	public ServiceEntityService getServiceEntityService() {
		return serviceEntityService;
	}

	/**
	 * @param serviceEntityService
	 *            data access service object for Web services
	 */
	public void setServiceEntityService(
			ServiceEntityService serviceEntityService) {
		this.serviceEntityService = serviceEntityService;
	}

	/**
	 * @return notification module alert
	 */
	public IAlert getAlert() {
		return alert;
	}

	/**
	 * @param alert
	 *            notification module alert
	 */
	public void setAlert(IAlert alert) {
		this.alert = alert;
	}

}