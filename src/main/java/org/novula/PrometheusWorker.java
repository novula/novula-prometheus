package org.novula;

import org.novula.configuration.ConfigurationMap;
import org.novula.statemachine.StateMachineFactory;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

public class PrometheusWorker implements Callable<Void>
{
	private final ExecutorService executorService;
	private final ConfigurationMap configuration;
	private final StateMachineFactory<Integer> stateMachineFactory;

	public PrometheusWorker(final ExecutorService executorService, final ConfigurationMap configuration,
			final StateMachineFactory<Integer> stateMachineFactory) {
		this.executorService = executorService;
		this.configuration = configuration;
		this.stateMachineFactory = stateMachineFactory;
	}

	@Override
	public Void call() throws Exception
	{
		final int prometheusPort = configuration.getInt("prometheus-port");
		final ServerSocket serverSocket = new ServerSocket(prometheusPort);
		while (!serverSocket.isClosed())
		{
			final Socket client = serverSocket.accept();
			executorService.submit(new ConnectionHandler(client, stateMachineFactory.create()));
		}
		return null;
	}
}
