/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server_Side;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author linhtd99
 */
public class Server {
    
    private int port;
    private List<User> clients;
    private ServerSocket server;
    
    public static void main(String[] args) throws Exception {
        new Server(4444).run();
    }

    public Server(int port) {
        this.port = port;
        this.clients = new ArrayList<User>();
    }
    
    public void run() throws Exception {
        
        //Server Started
        server = new ServerSocket(port);
        System.out.println("Server Started ...");
        
        //Accept clients
        while(true){
            Socket client = server.accept();
            
            //Set nickname for client
            String nickname = (new Scanner(client.getInputStream())).nextLine();
            System.out.println("A new client connected with nickname: " + 
                    nickname + " and IP Address: " + client.getInetAddress().getHostAddress());
            
            //Create new user and add to List
            User newUser = new User(nickname, client);
            this.clients.add(newUser);
            
            //New thread for new user
            new Thread(new UserHandler(this, newUser)).start();
        } 
    }
    
    //Remove user
    public void removeUser(User user){
        this.clients.remove(user);
    }
    
    //Broadcast list of online users
    public void broadcastUser(){
        for(User client: this.clients){
            client.getOutStream().println(this.clients);
        }
    }
    
    //Broadcast msg to all users (1 - N)
    public void broadcastMsg(String msg, User user){
        for(User client: this.clients){
            client.getOutStream().println(user.toString() + ": " + msg);
        }
    }
    
    
    //Send msg to an user (1 - 1)
    public void sendMsgToUser(String msg, User sender, String rcver){
        
        boolean user_exists = false;
        
        for(User client: this.clients){
            if(client.getNickname().equals(rcver) && client != sender){
                user_exists = true;
                sender.getOutStream().println("(Private) To " + 
                        client.toString() + ": " + msg);
                client.getOutStream().println("(Private) From " + 
                        sender.toString() + ": " + msg);
            }
        }
        
        if(!user_exists){
            sender.getOutStream().println("[Server] User " + rcver + " doesn't exist!");
        }
    }
}
