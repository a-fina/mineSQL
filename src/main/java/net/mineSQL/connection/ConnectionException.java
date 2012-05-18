package net.mineSQL.connection;

public class ConnectionException extends Exception
{
	public ConnectionException() {
		super();
	}

	public ConnectionException(Exception e) {
		super(e);
	}
}
