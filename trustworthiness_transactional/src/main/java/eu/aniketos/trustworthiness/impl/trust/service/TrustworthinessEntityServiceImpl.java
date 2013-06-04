package eu.aniketos.trustworthiness.impl.trust.service;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import eu.aniketos.trustworthiness.impl.trust.pojo.TrustworthinessEntity;
import eu.aniketos.trustworthiness.trust.dao.TrustworthinessDao;
import eu.aniketos.trustworthiness.trust.service.TrustworthinessEntityService;

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

	public void addTrustworthiness(TrustworthinessEntity trustworthinessEntity) {
		trustworthinessDao.addTrustworthiness(trustworthinessEntity);
	}

	public void updateTrustworthiness(TrustworthinessEntity trustworthinessEntity) {
		trustworthinessDao.updateTrustworthiness(trustworthinessEntity);
	}

	@Transactional(readOnly = true)
	public TrustworthinessEntity getTrustworthiness(String source) {
		return trustworthinessDao.getTrustworthiness(source);
	}

	public void deleteTrustworthiness(TrustworthinessEntity trustworthinessEntity) {
		trustworthinessDao.deleteTrustworthiness(trustworthinessEntity);
	}

}