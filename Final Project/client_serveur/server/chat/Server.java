package chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import utils.UserErrorHandler;

/**
 * Permet de lancer le côté serveur de la communication
 */
public final class Server {

	private ServerSocket serverSocket;
	private List<PrintWriter> allOut;
	private volatile boolean threadActive;
	private volatile boolean streamActive;

	/**
	 * Initialise le côté serveur de la communication
	 */
	public Server() {
		threadActive = true;
		streamActive = true;

		try {
			allOut = new ArrayList<PrintWriter>();
			serverSocket = new ServerSocket(12291); //Port non attribué par IANA (Internet Assigned Numbers Authority)
			start();
		} catch(final Exception e) {
			new UserErrorHandler("An error occurred while launching the connection on the server side");
		}
	}

	/**
	 * Permet d'obtenir le socket du côté serveur de la communication
	 */
	public ServerSocket getServeurSocket() {
		return serverSocket;
	}

	/**
	 * Permet de terminer les threads lancer du côté serveur de la communication, et de lancer la fermuture des threads du côté client, ainsi que la fermeture des sockets
	 */
	private synchronized void endThreads() {
		if(!streamActive) {
			sendMessageToAllClient("end_stream");
			threadActive = false;
			close();
			Main.console.println("Sequence end");
		}
	}

	/**
	 * Permet de terminer les streams du côté serveur, puis de terminer les threads lancer du côté serveur de la communication, et de lancer la fermuture des threads du côté client, ainsi que la fermeture des sockets
	 */
	public synchronized void endStreams() {
		if(streamActive) {
			streamActive = false;
			endThreads();
		}
	}

	/**
	 * Permet d'ajouter un PrintWriter correspondant au stream serveur à client pour un nouveau client
	 */
	private synchronized void addOut(final PrintWriter pw) {
		allOut.add(pw);
	}

	/**
	 * Permet d'envoyer un message à tous les clients
	 */
	private synchronized void sendMessageToAllClient(final String m) {
		for(final PrintWriter pw : allOut) {
			pw.println(m);
		}
	}

	/**
	 * Lance le serveur
	 */
	private void start() {
		new Thread(new WaintingForClientHandler()).start();
	}

	/**
	 * Cloture le serveur
	 */
	private synchronized void close() {
		if(!threadActive) {
			try {
				serverSocket.close();
				sendMessageToAllClient("close_stream");
				Main.console.getConsoleStream().close();
			} catch(final IOException e) {
				new UserErrorHandler("An error occurred while closing the server side");
			}
		}
	}

	/**
	 * Permet de mettre en place la communication avec des nouveaux clients
	 */
	private final class WaintingForClientHandler implements Runnable {

		@Override
		public void run() {
			try {
				while(threadActive) {
					Main.console.println("Waiting for a connexion");
					Socket socket;
					socket = serverSocket.accept();
					Main.console.println("Connection established on the server side");
					new Thread(new ServerToClientHandler(socket)).start();
					new Thread(new ClientToServerHandler(socket)).start();
				}
			} catch(final IOException e) {}
		}
	}

	/**
	 * Pour chaque client, permet la mise en place de la communication du serveur au client
	 */
	private final class ServerToClientHandler implements Runnable {

		private final Socket socket;
		private final String host;

		public ServerToClientHandler(final Socket socket) {
			this.socket = socket;
			host = socket.getInetAddress().getHostAddress();
		}

		@Override
		public void run() {
			OutputStream out = null;
			OutputStreamWriter osw = null;
			PrintWriter pw = null;

			try {
				out = socket.getOutputStream();
				osw = new OutputStreamWriter(out,"UTF-8");
				pw = new PrintWriter(osw,true);
				addOut(pw);
				sendMessageToAllClient(host + " online");
				while(streamActive) {}
				sendMessageToAllClient("end_stream");
			} catch(final IOException e) {
				new UserErrorHandler("An error occurred on the server side in the communication from server to client");
			} finally {
				if(pw != null) {
					pw.close();
				} else {
					if(osw != null) {
						try {
							osw.close();
						} catch(final IOException e) {}
					} else {
						if(out != null) {
							try {
								out.close();
							} catch(final IOException e) {}
						}
					}
				}
			}
		}
	}

	/**
	 * Pour chaque client, permet la mise en place de la communication du client au serveur
	 */
	private final class ClientToServerHandler implements Runnable {

		private final Socket socket;
		private final String host;

		public ClientToServerHandler(final Socket socket){
			this.socket = socket;
			host = socket.getInetAddress().getHostAddress();
		}

		@Override
		public void run() {
			InputStream in = null;
			InputStreamReader isr = null;
			BufferedReader br = null;

			try {
				in = socket.getInputStream();
				isr = new InputStreamReader(in,"utf-8");
				br = new BufferedReader(isr);

				String message = null;

				while(threadActive) {
					if((message = br.readLine()) != null) {
						if(message.equals("end_stream")) {
							br.close();
							endStreams();
						} else {
							Main.console.println("Server received " + message + " from " + host);
							sendMessageToAllClient(host + " has sent " + message);
						}
					}
				}
			} catch(final IOException e) {
				new UserErrorHandler("An error occurred on the server side in the communication from client to server");
			} finally {
				if(br != null) {
					try {
						br.close();
					} catch(final IOException e) {}
				} else {
					if(isr != null) {
						try {
							isr.close();
						} catch(final IOException e) {}
					} else {
						if(in != null) {
							try {
								in.close();
							} catch(final IOException e) {}
						}
					}
				}
			}
		}
	}
}