/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client_Side;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author linhtd99
 */
public class RcvMsgHandler implements Runnable {
    
    private InputStream server;

    public RcvMsgHandler(InputStream server) {
        this.server = server;
    }
    
    @Override
    public void run() {
        Scanner sc = new Scanner(server);
        String tmp = "";
        while(sc.hasNextLine()){
            tmp = sc.nextLine();
            if(tmp.charAt(0) == '['){
                tmp = tmp.substring(1, tmp.length() - 1);
                System.out.println("[Server] Online users: " + 
                        new ArrayList<String>(Arrays.asList(tmp.split(","))));
            }
            else{
                System.out.println(tmp);
            }
        }
    }
}
