package org.novula;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NovulaPrometheus
{
	public static void main(final String[] args) throws IOException
	{
		final ExecutorService executorService = Executors.newCachedThreadPool();

		final int prometheusPort = 8080;
		final ServerSocket serverSocket = new ServerSocket(prometheusPort);
		final Socket client = serverSocket.accept();

		executorService.submit(new ConnectionHandler(client));
	}
}
