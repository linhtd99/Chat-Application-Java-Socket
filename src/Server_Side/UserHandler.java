/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server_Side;

import java.util.Scanner;

/**
 *
 * @author linhtd99
 */
public class UserHandler implements Runnable {
    
    //Incoming user msg handling
    private Server server;
    private User user;

    public UserHandler(Server server, User user) {
        this.server = server;
        this.user = user;
        this.server.broadcastUser();
    }
    
    @Override
    public void run() {
        String msg;
        
        Scanner sc = new Scanner(this.user.getInStream());
        while(sc.hasNextLine()){
            msg = sc.nextLine();
            
            if(msg.charAt(0) == '@'){
                if(msg.contains(" ")){
                    int firstSpace = msg.indexOf(" ");
                    String rcver = msg.substring(1, firstSpace);
                    server.sendMsgToUser(msg.substring(firstSpace + 1, msg.length()), 
                            user, rcver);
                }
            }
            
            else{
                server.broadcastMsg(msg, user);
            }
        }
        
        server.removeUser(user);
        this.server.broadcastUser();
        sc.close();
    }
}
