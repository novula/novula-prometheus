package org.novula;

import org.novula.configuration.ConfigurationMap;
import org.novula.configuration.ConfigurationReader;
import org.novula.statemachine.PrometheusStateMachineFactory;
import org.novula.statemachine.StateMachineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NovulaPrometheus
{
	private static final Logger LOGGER = LoggerFactory.getLogger(NovulaPrometheus.class);

	public static void main(final String[] args)
	{
		LOGGER.info("Starting Prometheus...");

		final ExecutorService executorService = Executors.newCachedThreadPool();
		final ConfigurationMap configuration = ConfigurationReader.newJsonReader("prometheus.json").read().asMap();
		final StateMachineFactory<Integer> stateMachineFactory = new PrometheusStateMachineFactory();

		final PrometheusWorker worker = new PrometheusWorker(executorService, configuration, stateMachineFactory);
		try
		{
			worker.call();
		}
		catch (final Exception e)
		{
			LOGGER.error("An internal error occurred.", e);
		}

		LOGGER.info("Prometheus finishes his work.");
	}
}
