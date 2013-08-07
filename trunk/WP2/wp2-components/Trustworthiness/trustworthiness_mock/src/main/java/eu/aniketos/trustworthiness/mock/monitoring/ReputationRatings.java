/**
 * Copyright (c) 2013, Waterford Institute of Technology
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met
 *    - Redistributions of source code must retain the above copyright
 *      notice, this list of conditions and the following disclaimer.
 *    - Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *    - Neither the name of Waterford Institute of Technology nor the
 *      names of its contributors may be used to endorse or promote products
 *      derived from this software without specific prior written permission.
 *      
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL WATERFORD INSTITUTE OF TECHNOLOGY BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package eu.aniketos.trustworthiness.mock.monitoring;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import eu.aniketos.trustworthiness.ext.messaging.IReputationRatingsService;
import eu.aniketos.trustworthiness.ext.rules.model.event.ConsumerRatingEvent;

/**
 * @author Hisain Elshaafi (TSSG)
 * 
 */
public class ReputationRatings {

	private static Logger logger = Logger.getLogger(ReputationRatings.class);

	private IReputationRatingsService repMetrics;

	/**
	 * @param qosMetics
	 */
	public ReputationRatings(IReputationRatingsService repMetrics) {
		super();
		this.repMetrics = repMetrics;
	}

	/**
	 * 
	 */
	public void initialize() {

		// sending metrics for Aniketos sample services
		logger.info("Sending reputation ratings for Aniketos sample services");

		Scanner scanner = null;

		try {

			scanner = new Scanner(new File("data/reputation_data.txt"));

		} catch (FileNotFoundException e) {
			logger.error(e.getMessage());
		}

		while (scanner != null && scanner.hasNext()) {
			String line = scanner.next();

			String[] qosData = line.split(",");

			ConsumerRatingEvent event = new ConsumerRatingEventImpl();

			event.setServiceId(qosData[0]);

			event.setProperty(qosData[1]);

			event.setConsumerId(qosData[2]);

			event.setTransactionId(qosData[3]);

			event.setValue(qosData[4]);

			DateTime dt = new DateTime();
			DateTimeFormatter fmt = ISODateTimeFormat.dateTimeNoMillis();
			String timestamp = fmt.print(dt);

			event.setTimestamp(timestamp);

			try {
				repMetrics.processReputationRating(event);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}

			if (logger.isDebugEnabled()) {
				logger.debug("added reputation for service: " + qosData[0]);
			}
		}

	}
}
