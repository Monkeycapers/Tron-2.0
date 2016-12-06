package Jesty.TCPBridge;

import java.net.InetAddress;
import java.util.ArrayList;


/**
 * Created by Evan on 10/19/2016.
 *
 * A Server class that can handle and send messages to multiple clients
 * Extend this to use it in your project
 */
public class Server {

    protected Clients clients;

    int raw_port, web_port;

    public Server(int raw_port, int web_port) {
        this.web_port = web_port;
        this.raw_port = raw_port;
    }

    public Server() {
        raw_port = 16000; web_port = 8080;
    }


    public void start() {
        clients = new Clients(this);
        ClientFactory clientFactory = new ClientFactory(clients, raw_port);
        WebFactory webFactory = new WebFactory(clients, web_port);
        webFactory.start();
        new Thread(clientFactory).start();
        try {
            //Cut of the PC name part of the ip address
            System.out.println("The IP Adress of the Server: " + InetAddress.getLocalHost().toString().split("/")[1]);
        }
        catch (Exception e) {
            System.out.println("The IP Adress of the Server: localhost");
        }
        //
    }

    public void onMessage(ClientWorker clientWorker, String message) {
        System.out.println(clientWorker.id + " " + message);
        for (ClientWorker client: clients.getList()) {
            client.sendMessage(message);
        }
    }
    public void onClose(ClientWorker clientWorker, int code) {
        clients.getList().remove(clientWorker);
    }
    public void onOpen(ClientWorker clientWorker, int code) {

    }

}
