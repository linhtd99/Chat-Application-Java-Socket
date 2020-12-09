/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client_Side;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 *
 * @author linhtd99
 */
public class Client {
    
    private String host;
    private int port;
    
    public static void main(String[] args) throws Exception {
        new Client("127.0.0.1", 4444).run();
    }
    
    public Client(String host, int port){
        this.host = host;
        this.port = port;
    }
    
    public void run() throws Exception {
        Socket client = new Socket(host, port);
        System.out.println("Connected ...");
        
        PrintStream output = new PrintStream(client.getOutputStream());
        
        //Create nickname for user
        Scanner sc = new Scanner(System.in);
        System.out.print("Create your nickname: ");
        String nickname = sc.nextLine();
        
        //Send nickname to server
        output.println(nickname);

        //New thread for receive message handling
        new Thread(new RcvMsgHandler(client.getInputStream())).start();
        
        //Read msg from keyboard and send to server
        while(sc.hasNextLine()){
            output.println(sc.nextLine());
        }
        
        output.close();
        sc.close();
        client.close();
    }
}
