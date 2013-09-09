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
package eu.aniketos.trustworthiness.impl.messaging.util;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import eu.aniketos.trustworthiness.configuration.ConfigurationManagement;
import eu.aniketos.trustworthiness.impl.trust.pojo.Rating;
import eu.aniketos.trustworthiness.trust.service.RatingEntityService;

public class PropertyValidator {

	private static Logger logger = Logger.getLogger(PropertyValidator.class);

	private RatingEntityService ratingEntityService;

	private ConfigurationManagement config;

	private static HashMap<String, Long> consumers = new HashMap<String, Long>();

	private static long lastRefresh = 0;

	public PropertyValidator(RatingEntityService ratingEntityService,
			ConfigurationManagement config) {
		super();
		this.ratingEntityService = ratingEntityService;
		this.config = config;
	}

	public boolean isNumeric(String str) {

		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public boolean isValidConsumer(String consumerId) {

		boolean valid = false;

		int allowedInterval = config.getConfig().getInt(
				"allowed_consumer_interval");

		long now = System.currentTimeMillis() / 3600000;

		// refresh consumer list every hour
		if (lastRefresh - now > -1) {
			for (String consumer : consumers.keySet()) {

				long timeSinceLastVote = consumers.get(consumer) - now;

				if (timeSinceLastVote > allowedInterval) {
					consumers.remove(consumer);
				}
			}
			lastRefresh = now;
		}

		if (consumers.containsKey(consumerId)) {
			
			logger.warn("consumer rating less than allowed interval "
					+ consumerId);
			return valid = false;
		}

		List<Rating> consumerRatings = ratingEntityService
				.getRatingByConsumerId(consumerId);

		if (consumerRatings == null || consumerRatings.isEmpty()) {

			logger.info("new consumer");
			consumers.put(consumerId, now);

			valid = true;

		} else {

			logger.info("existing consumer");

			int latest = 0;

			for (Rating rating : consumerRatings) {
				long recency = rating.getRecency();
				if (latest > recency) {
					latest = (int) recency;
				}
			}

			long timeSinceLastVote = latest - now;

			if (timeSinceLastVote > allowedInterval) {

				consumers.put(consumerId, now);
				valid = true;

			} else {

				logger.warn("consumer rating less than allowed interval "
						+ consumerId);

			}
		}
		return valid;
	}

}
