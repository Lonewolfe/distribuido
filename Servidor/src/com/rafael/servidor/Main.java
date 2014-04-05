package com.rafael.servidor;

import java.io.IOException;

import com.rafael.servidor.server.Server;

public class Main {

	public static void main(String[] args) {
		try {
			final Server server = Server.getInstance();
			final Thread thread = new Thread(server);
			thread.start();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
