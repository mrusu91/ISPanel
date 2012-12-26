package com.vrx.ispanel;

public class ServerHandler implements Runnable{

	public Server server;
	
	@Override
	public void run() {
		server = new Server();
		server.start(); // loop until server.stop() is called
	}

	public void closeServer(){
		server.stop();
	}
}
