package eu.aniketos.wp2.components.trustworthiness.impl.trust.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.aniketos.wp2.components.trustworthiness.impl.trust.pojo.Trustworthiness;
import eu.aniketos.wp2.components.trustworthiness.trust.service.TrustworthinessEntityService;
import eu.aniketos.wp2.components.trustworthiness.trust.dao.TrustworthinessDao;

/**
 * data access service for atomic and composite Web service trustworthiness
 * 
 * @author Hisain Elshaafi (TSSG)
 * 
 */
@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = { Exception.class })
public class TrustworthinessEntityServiceImpl implements
		TrustworthinessEntityService {

	TrustworthinessDao trustworthinessDao;

	public TrustworthinessEntityServiceImpl(
			TrustworthinessDao trustworthinessDao) {
		super();
		this.trustworthinessDao = trustworthinessDao;
	}

	public void addTrustworthiness(Trustworthiness trustworthiness) {
		trustworthinessDao.addTrustworthiness(trustworthiness);
	}

	public void updateTrustworthiness(Trustworthiness trustworthiness) {
		trustworthinessDao.updateTrustworthiness(trustworthiness);
	}

	@Transactional(readOnly = true)
	public Trustworthiness getTrustworthiness(String source) {
		return trustworthinessDao.getTrustworthiness(source);
	}

	public void deleteTrustworthiness(Trustworthiness trustworthiness) {
		trustworthinessDao.deleteTrustworthiness(trustworthiness);
	}

}
