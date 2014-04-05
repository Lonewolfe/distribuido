package com.rafael.servidor.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;
import java.util.UUID;

public class Client extends Thread {

	/** Identificador desta conexão. */
	private String ident;

	/** Se o cliente ainda esta ativo. */
	private boolean isAlive;

	/** Conexão */
	private final Socket socket;

	/** Canal de entrada. */
	private BufferedReader istream;

	/** Canal de saída. */
	private BufferedWriter ostream;

	/**
	 * Construtor.
	 * 
	 * @param socket
	 *            Conexão com o cliente
	 * @throws IOException
	 *             falha ao obter as streams de io
	 */
	public Client(final Socket socket) throws IOException {
		this.socket = socket;
		if (socket.isConnected()) {
			this.isAlive = true;
			this.istream = new BufferedReader(new InputStreamReader(
					this.socket.getInputStream()));
			this.ostream = new BufferedWriter(new OutputStreamWriter(
					this.socket.getOutputStream()));
			this.ident = UUID.randomUUID().toString();
		}
	}

	/**
	 * Execução da Thread.
	 */
	public void run() {
		try {
			while (this.isAlive) {
				final String cmd = this.istream.readLine();
				// do something
				final String response = cmd + " >> " + new Date();
				this.send(response);
			}

			this.shutdown();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Envia uma mensagem ao cliente.
	 * 
	 * @param message
	 *            msg
	 * @throws IOException
	 *             falha
	 */
	public void send(final String message) throws IOException {
		this.ostream.write(message);
	}

	/**
	 * Encerra o cliente.
	 */
	public void close() {
		this.isAlive = false;
	}

	/**
	 * Encerra os objetos internos.
	 */
	private void shutdown() {
		try {
			this.send("A conexao sera encerrada.");
			this.istream.close();
			this.ostream.close();
			this.socket.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Getter.
	 * 
	 * @return ident
	 */
	public String getIdent() {
		return ident;
	}

}
