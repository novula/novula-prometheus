package org.novula;

import org.novula.statemachine.StateMachine;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

class ConnectionHandler implements Runnable
{
	private final StateMachine<Integer> stateMachine;
	private final Socket client;

	public ConnectionHandler(final Socket client, final StateMachine<Integer> stateMachine)
	{
		this.client = client;
		this.stateMachine = stateMachine;
	}

	public void run()
	{
		try
		{
			final DataInput reader = new DataInputStream(client.getInputStream());
			while (client.isConnected())
			{
				final int messageLength = reader.readUnsignedShort();
				final byte[] buffer = new byte[messageLength];
				reader.readFully(buffer);

				final DataInput dataInput = new DataInputStream(new ByteArrayInputStream(buffer));

				processMessage(messageLength, dataInput);
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void processMessage(final int messageLength, final DataInput dataInput) throws IOException
	{
		final int messageType = dataInput.readUnsignedShort();
		stateMachine.actuate(messageType);
	}
}
