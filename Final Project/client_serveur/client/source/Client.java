package com.example.fixe1to.myapplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.AsyncTask;

import com.example.fixe1to.myapplication.utils.UserErrorHandler;

/**
 * Permet de lancer le côté client de la communication
 */
public final class Client extends AsyncTask<MainActivity, Void, Void> {

    private volatile MainActivity context;
    private Socket clientSocket;
    private PrintWriter pw;
    private volatile boolean threadActive;

    /**
     * Initialise le côté client de la communication
     */
    @Override
    protected Void doInBackground(MainActivity[] contextArray) {
        context = contextArray[0];
        threadActive = true;

        try {
            context.getDisplay().setText("Launching connection on the client side");
            clientSocket = new Socket("192.168.0.18",12291);
            context.getDisplay().setText("Connection established on the client side");
            start();
        } catch(final Exception e) {
            new UserErrorHandler(context,"An error occurred while launching the connection on the client side");
        }
        return null;
    }

    /**
     * Permet d'obtenir le socket du côté client de la communication
     */
    public Socket getClientSocket() {
        return clientSocket;
    }

    /**
     * Permet de terminer les threads lancer du côté client de la communication
     */
    private synchronized void endThreads() {
        threadActive = false;
    }

    /**
     * Lance le client
     */
    private void start() {
        if(clientSocket != null) {
            new Thread(new ClientToServerHandler()).start();
            new Thread(new ServerToClientHandler()).start();
        }
    }

    /**
     * Cloture le client
     */
    private synchronized void close() {
        if(!threadActive) {
            try {
                clientSocket.close();
            } catch(final IOException e) {
                new UserErrorHandler(context,"An error occurred while closing the client side");
            }
        }
    }

    /**
     * Permet la mise en place de la communication du client au serveur
     */
    private final class ClientToServerHandler implements Runnable {

        @Override
        public void run() {
            OutputStream out = null;
            OutputStreamWriter osw = null;

            try {
                out = clientSocket.getOutputStream();
                osw = new OutputStreamWriter(out,"utf-8");
                pw = new PrintWriter(osw,true);

                while(threadActive) {
                    if(!context.getDataToSend().isEmpty()) {
                        pw.println(context.getDataToSend());
                        context.setDataToSend("");
                    }
                }
            } catch(final IOException e) {
                new UserErrorHandler(context,"An error occurred on the client side in the communication from client to server");
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
     * Permet la mise en place de la communication du serveur au client
     */
    private final class ServerToClientHandler implements Runnable {

        @Override
        public void run() {
            InputStream in = null;
            InputStreamReader isr = null;
            BufferedReader br = null;

            try {
                in = clientSocket.getInputStream();
                isr = new InputStreamReader(in,"utf-8");
                br = new BufferedReader(isr);
                String message = null;

                while(threadActive) {
                    if((message = br.readLine()) != null) {
                        if(message.equals("end_stream") || message.equals("close_stream")) {
                            if(message.equals("end_stream")) {
                                endThreads();
                            }
                            if(message.equals("close_stream")) {
                                close();
                            }
                        } else {
                            context.runOnUiThread(new RunnableDisplay(message));
                        }
                    }
                }
            } catch(final IOException e) {
                new UserErrorHandler(context,"An error occurred on the client side in the communication from server to client");
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

    private final class RunnableDisplay implements Runnable {

        private final String message;

        RunnableDisplay(String message) {
            this.message = message;
        }

        public void run() {
            context.getDisplay().setText("Received message : " + message);
        }
    }
}