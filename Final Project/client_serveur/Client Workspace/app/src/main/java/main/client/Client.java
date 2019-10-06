package main.client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import android.os.AsyncTask;

import main.client.utils.Constants;
import main.client.utils.FileComboItem;
import main.client.utils.UserErrorHandler;
import main.client.utils.ValidationMethods;

/**
 * Permet de lancer le côté client de la communication
 */
public final class Client extends AsyncTask<MainActivity, Void, Void> {

    private List<FileComboItem> index;
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
            clientSocket = new Socket("192.168.0.18",12292);
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

    private synchronized void sendMessageToServer(final String m) {
        if(pw != null) {
            pw.println(m);
        }
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
                        sendMessageToServer(context.getDataToSend());
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
            boolean isReadingIndex = false;
            boolean indexWasRead = false;
            List<Integer> indexEdit = null;

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
                            } else {
                                if(message.equals("close_stream")) {
                                    close();
                                } else {
                                    if(ValidationMethods.isClassif(message) && indexWasRead) {

                                    } else {
                                        if(indexWasRead) {
                                            if(ValidationMethods.isIndexInitializer(message)) {
                                                isReadingIndex = true;
                                                indexEdit = new ArrayList<>();
                                            } else {
                                                if (isReadingIndex) {
                                                    if (ValidationMethods.isIndexEnd(message)) {
                                                        final int len = indexEdit.size();
                                                        for (int i = 0; i < len; i++) {
                                                            sendMove(indexEdit.get(i), index);
                                                        }
                                                        isReadingIndex = false;
                                                        indexWasRead = true;
                                                        indexEdit = null;
                                                    } else {
                                                        if (ValidationMethods.isValidFile(message)) {
                                                            indexEdit.add(Integer.parseInt(message));
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
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

        RunnableDisplay(final String message) {
            this.message = message;
        }

        public void run() {
            context.getDisplay().setText("Received message : " + message);
        }
    }

    public void setIndex(final List<FileComboItem> index) {
        this.index = index;
    }

    public void checkIndex(final List<FileComboItem> index) {
        setIndex(index);
        sendMessageToServer("file=>indexInit");
        final int len = index.size();
        for(int i = 0; i<len; i++) {
            sendMessageToServer(encodeIndex(i,index.get(i)));
        }
        sendMessageToServer("file=>indexEnd");
    }

    private String encodeIndex(final int id, final FileComboItem indexElement) {
        return id + " => " + indexElement.getMD5() + "&" + indexElement.getFileName();
    }

    private void sendMove(final int id, final List<FileComboItem> index) {
        final File move = new File(Constants.BASE_MOVE + index.get(id).getFileName());
        if(move.exists()) {
            Scanner sc = null;

            try {
                sc = new Scanner(move);

                while(sc.hasNextLine()) {
                    sendMessageToServer(sc.nextLine());
                }
            } catch(IOException e) {
                //shutdown
            } finally {
                if(sc != null) {
                    sc.close();
                }
            }
        } else {
            //shutdown
        }
    }

    public void sendActiveMove(final int id) {
        sendMessageToServer("move=>" + id);
    }
}