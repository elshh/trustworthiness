package eu.aniketos.wp2.components.trustworthiness.impl.trust.management.composite;

import java.util.Set;

import org.apache.log4j.Logger;

import eu.aniketos.wp2.components.trustworthiness.impl.trust.management.atomic.ServiceTrustworthiness;
import eu.aniketos.wp2.components.trustworthiness.impl.trust.pojo.Composite;
import eu.aniketos.wp2.components.trustworthiness.impl.trust.pojo.Atomic;
import eu.aniketos.wp2.components.trustworthiness.trust.management.atomic.ServiceTrustUpdatePolicy;
import eu.aniketos.wp2.components.trustworthiness.trust.management.atomic.Trustworthiness;
import eu.aniketos.wp2.components.trustworthiness.trust.management.composite.CompositeTrustUpdate;
import eu.aniketos.wp2.components.trustworthiness.trust.service.ServiceEntityService;

/**
 * @author Hisain Elshaafi (TSSG)
 *
 */
public class CompositeTrustUpdateImpl implements CompositeTrustUpdate {

	private static Logger logger = Logger.getLogger(CompositeTrustUpdateImpl.class);
	
	ServiceEntityService serviceEntityService;
	
	ServiceTrustUpdatePolicy trustUpdate;
	
	public Trustworthiness aggregateTrustworthiness(String serviceId) throws Exception{
		
		Composite compositeService = serviceEntityService.getComposite(serviceId);
		
		Trustworthiness tw = new ServiceTrustworthiness();
		
		double twScore = 1;
		double twConfidence = 1;
		
		/**
		 * TODO: update component trustworthiness before aggregation
		 */
		Set<Atomic> componentServices = compositeService.getComponentServices();
		if (componentServices==null || componentServices.size()==0){
			logger.error("no component service found for " + serviceId);
		}
		
		
		//TODO update atomic trust before aggregation
		for (Atomic service : componentServices){
			logger.debug("component service " + service.getId());
			tw = trustUpdate.calculateTrust(service.getId());
			
			double score = tw.getScore();
			double confidence = tw.getConfidence();
			
			if (logger.isDebugEnabled()){
				logger.debug(service + " trustworthiness " + score + "," + confidence);
			}
			twScore *= score;
			twConfidence *= confidence;
		}
		
		tw.setScore(twScore);
		tw.setConfidence(twConfidence);
		
		return tw;
	
	}
	
	public ServiceEntityService getServiceEntityService() {
		return serviceEntityService;
	}

	public void setServiceEntityService(ServiceEntityService sEntityService) {
		this.serviceEntityService = sEntityService;
	}
	
	public ServiceTrustUpdatePolicy getTrustUpdate() {
		return trustUpdate;
	}

	public void setTrustUpdate(ServiceTrustUpdatePolicy trustUpdate) {
		this.trustUpdate = trustUpdate;
	}


}
