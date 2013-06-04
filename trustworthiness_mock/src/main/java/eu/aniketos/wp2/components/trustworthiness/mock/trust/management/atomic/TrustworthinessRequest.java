package eu.aniketos.wp2.components.trustworthiness.mock.trust.management.atomic;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.log4j.Logger;

import eu.aniketos.wp2.components.trustworthiness.ext.messaging.ICompositeTrustworthinessPrediction;
import eu.aniketos.wp2.components.trustworthiness.ext.messaging.ITrustworthinessPrediction;
import eu.aniketos.wp2.components.trustworthiness.ext.messaging.Trustworthiness;

/**
 * @author Hisain Elshaafi (TSSG)
 * 
 */
public class TrustworthinessRequest {

	private static Logger logger = Logger
			.getLogger(TrustworthinessRequest.class);

	ITrustworthinessPrediction twPrediction;
	ICompositeTrustworthinessPrediction ctwPrediction;

	/**
	 * @param twPrediction
	 * @param ctwPrediction
	 */
	public TrustworthinessRequest(ITrustworthinessPrediction twPrediction,
			ICompositeTrustworthinessPrediction ctwPrediction) {
		super();
		this.twPrediction = twPrediction;
		this.ctwPrediction = ctwPrediction;
	}

	/**
	 * 
	 */
	public void requestTrustworthiness() {
		
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File("data\\services_data.txt"));

		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		}
		

		while (scanner != null && scanner.hasNext()) {
			
			String line = scanner.nextLine();

			String serviceId = line;

			Trustworthiness trustworthiness = twPrediction.getTrustworthiness(line);

			if (logger.isDebugEnabled()) {
				logger.debug("recevied trustworthiness for service: " + serviceId + "=" + trustworthiness);
			}
		}
	}
}
