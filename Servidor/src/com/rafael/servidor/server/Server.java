package com.rafael.servidor.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.rafael.servidor.client.Client;

public class Server implements Runnable {

	/** Se o servidor esta ativo. */
	private boolean isAlive;

	/** Singleton. */
	private static Server singleton;

	/** Receptor de conexões de clientes. */
	private ServerSocket ssocket;

	/** Conjuntos de clientes conectados. */
	private Map<String, Client> clients;

	/**
	 * Construtor.
	 * @throws IOException falha ao inicializar o server socket
	 */
	private Server() throws IOException {
		this.isAlive = true;
		this.ssocket = new ServerSocket(65432);
		this.clients = new HashMap<String, Client>();
	}

	/**
	 * Retorna a única instância do servidor.
	 * 
	 * @return o singleton
	 * @throws IOException falha ao inicializar o server socket
	 */
	public static Server getInstance() throws IOException {
		if (singleton == null) {
			singleton = new Server();
		}
		return singleton;
	}

	/**
	 * Execução da Thread.
	 */
	public void run() {
		while (this.isAlive) {
			try {
				final Socket socket = ssocket.accept();
				final Client client = new Client(socket);
				this.clients.put(client.getName(), client);
				this.broadcast("Client [" + client.getIdent() + "] entrou.");
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

	/**
	 * Comunicar a todos os clientes.
	 * 
	 * @param message
	 *            texto a ser propagado
	 */
	private void broadcast(final String message) {
		final Iterator<Client> it = this.clients.values().iterator();
		while (it.hasNext()) {
			try {
				it.next().send(message);
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
	}

}
