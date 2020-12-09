/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server_Side;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author linhtd99
 */
public class User {
    private static int nbUser = 0;
    private int userId;
    private PrintStream outStream;
    private InputStream inStream;
    private String nickname;
    private Socket client;
    //private String color;

    public User(String name, Socket client) throws IOException {
        this.outStream = new PrintStream(client.getOutputStream());
        this.inStream = client.getInputStream();
        this.client = client;
        this.nickname = name;
        this.userId = nbUser;
        //this.color = ColorInt.getColor(this.userId);
        nbUser += 1;
    }

    public PrintStream getOutStream() {
        return outStream;
    }

    public InputStream getInStream() {
        return inStream;
    }

    public String getNickname() {
        return nickname;
    }
    
    public String toString(){
        return this.getNickname();
    }
}
