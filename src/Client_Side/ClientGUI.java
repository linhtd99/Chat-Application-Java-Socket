package Client_Side;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.*;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.html.*;

import java.util.ArrayList;
import java.util.Arrays;


public class ClientGUI extends Thread{

    final JTextPane jtextFilDiscu = new JTextPane();
    final JTextPane jtextListUsers = new JTextPane();
    final JTextField jtextInputChat = new JTextField();
    private String oldMsg = "";
    private Thread read;
    private String serverIP;
    private int serverPORT;
    private String name;
    BufferedReader input;
    PrintWriter output;
    Socket server;
    
    public static void main(String[] args) throws Exception {
        ClientGUI client = new ClientGUI();
    } 

    public ClientGUI() {
    
        this.serverIP = serverIP;
        this.serverPORT = serverPORT;
        this.name = name;

        final JFrame jfr = new JFrame("Chat Application");
        jfr.getContentPane().setLayout(null);
        jfr.setSize(715, 600);
        jfr.setResizable(false);
        jfr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Config jtextFilDiscu
        jtextFilDiscu.setBounds(25, 25, 490, 320);
        jtextFilDiscu.setMargin(new Insets(6, 6, 6, 6));
        jtextFilDiscu.setEditable(false);
        JScrollPane jtextFilDiscuSP = new JScrollPane(jtextFilDiscu);
        jtextFilDiscuSP.setBounds(25, 25, 490, 320);
        jtextFilDiscu.setContentType("text/html");
        jtextFilDiscu.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

        //Config jtextListUsers
        jtextListUsers.setBounds(520, 25, 156, 320);
        jtextListUsers.setEditable(true);
        jtextListUsers.setMargin(new Insets(6, 6, 6, 6));
        jtextListUsers.setEditable(false);
        JScrollPane jsplistuser = new JScrollPane(jtextListUsers);
        jsplistuser.setBounds(520, 25, 156, 320);
        jtextListUsers.setContentType("text/html");
        jtextListUsers.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, true);

        //Config jtextInputChat
        jtextInputChat.setBounds(0, 350, 400, 50);
        jtextInputChat.setMargin(new Insets(6, 6, 6, 6));
        final JScrollPane jtextInputChatSP = new JScrollPane(jtextInputChat);
        jtextInputChatSP.setBounds(25, 420, 650, 50);
        
        //Config label
        final JLabel jbsoantin = new JLabel("Type your message here");
        jbsoantin.setBounds(25, 380, 150, 35);
        
        //Config send button
        final JButton jsbtn = new JButton("Send");
        jsbtn.setBounds(575, 480, 100, 35);

        //Config disconnect button
        final JButton jsbtndeco = new JButton("Disconnect");
        jsbtndeco.setBounds(25, 480, 130, 35);
        
        
        jtextInputChat.addKeyListener(new KeyAdapter() {
            //Send message on Enter
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                  sendMessage();
                }

                // Get last message typed
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    String currentMessage = jtextInputChat.getText().trim();
                    jtextInputChat.setText(oldMsg);
                    oldMsg = currentMessage;
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    String currentMessage = jtextInputChat.getText().trim();
                    jtextInputChat.setText(oldMsg);
                    oldMsg = currentMessage;
                }
            }
        });

        //Click on send button
        jsbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                sendMessage();
            }
        });

        //Connection view
        final JLabel jbIP = new JLabel("Server IP");
        final JLabel jbserverPORT = new JLabel("Server Port");
        final JLabel jbNAME =new JLabel("Your nickname");
        final JTextField jtfName = new JTextField(this.name);
        final JTextField jtfport = new JTextField(Integer.toString(this.serverPORT));
        final JTextField jtfAddr = new JTextField(this.serverIP);
        final JButton jcbtn = new JButton("Connect");

        //Config position
        jbIP.setBounds(25, 380, 135, 35);
        jbserverPORT.setBounds(200, 380, 135, 35);
        jbNAME.setBounds(375, 380, 135, 40);
        jtfAddr.setBounds(25, 420, 135, 35); 
        jtfName.setBounds(375, 420, 135, 35); 
        jtfport.setBounds(200, 420, 135, 35); 
        jcbtn.setBounds(575, 420, 100, 35); 

        //Set background for jtextFilDiscu and jtextListUsers
        jtextFilDiscu.setBackground(Color.LIGHT_GRAY);
        jtextListUsers.setBackground(Color.LIGHT_GRAY);

        //Add elements
        jfr.add(jbIP);
        jfr.add(jbserverPORT);
        jfr.add(jbNAME);
        jfr.add(jcbtn);
        jfr.add(jtextFilDiscuSP);
        jfr.add(jsplistuser);
        jfr.add(jtfName);
        jfr.add(jtfport);
        jfr.add(jtfAddr);
        jfr.setVisible(true);

        //On connect
        jcbtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                
                try {
                    name = jtfName.getText();
                    String port = jtfport.getText();
                    serverIP = jtfAddr.getText();
                    serverPORT = Integer.parseInt(port);
                    
                    jfr.setTitle("Chat Application - username: " + name);
                    server = new Socket(serverIP, serverPORT);

                    appendToPane(jtextFilDiscu, "<span>Connected to server at " + server.getRemoteSocketAddress()+"</span>");

                    input = new BufferedReader(new InputStreamReader(server.getInputStream()));
                    output = new PrintWriter(server.getOutputStream(), true);

                    //Send nickname to server
                    output.println(name);

                    //Create new Read Thread
                    read = new Read();
                    read.start();
                    jfr.remove(jbIP);
                    jfr.remove(jbserverPORT);
                    jfr.remove(jbNAME);
                    jfr.remove(jtfName);
                    jfr.remove(jtfport);
                    jfr.remove(jtfAddr);
                    jfr.remove(jcbtn);
                    jfr.add(jsbtn);
                    jfr.add(jtextInputChatSP);
                    jfr.add(jsbtndeco);
                    jfr.revalidate();
                    jfr.repaint();
                    jfr.add(jbsoantin);
                    jtextFilDiscu.setBackground(Color.WHITE);
                    jtextListUsers.setBackground(Color.WHITE);
                } 
                catch (Exception ex) {
                    appendToPane(jtextFilDiscu, "<span>Connect Error!</span>");
                    JOptionPane.showMessageDialog(jfr, ex.getMessage());
                }
            }
        });

        //On disconnect
        jsbtndeco.addActionListener(new ActionListener()  {
            @Override
            public void actionPerformed(ActionEvent ae) {
                jfr.add(jtfName);
                jfr.add(jtfport);
                jfr.add(jtfAddr);
                jfr.add(jcbtn);
                jfr.remove(jsbtn);
                jfr.remove(jtextInputChatSP);
                jfr.remove(jsbtndeco);
                jfr.revalidate();
                jfr.repaint();
                read.interrupt();
                jtextListUsers.setText(null);
                jtextFilDiscu.setBackground(Color.LIGHT_GRAY);
                jtextListUsers.setBackground(Color.LIGHT_GRAY);
                appendToPane(jtextFilDiscu, "<span>Disconnected</span>");
                output.close();
            }
        });
    }


    //Send message
    public void sendMessage() {
        try {
            String message = jtextInputChat.getText().trim();
            if (message.equals("")) {
                return;
            }
        this.oldMsg = message;
        output.println(message);
        jtextInputChat.requestFocus();
        jtextInputChat.setText(null);
        } 
        catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage());
            System.exit(0);
        }
    }

    //Read new incoming messages
    class Read extends Thread {
        @Override
        public void run() {
            String message;
            while(!Thread.currentThread().isInterrupted()){
            try {
                message = input.readLine();
                if(message != null){
                    if(message.charAt(0) == '[' && message.charAt(1) == 'S'){
                        appendToPane(jtextFilDiscu, message);
                    } 
                    else if (message.charAt(0) == '[') {
                        message = message.substring(1, message.length()-1);
                        ArrayList<String> ListUser = new ArrayList<String>(
                            Arrays.asList(message.split(", ")));
                        jtextListUsers.setText(null);
                        appendToPane(jtextListUsers, "Online Users");
                        for (String user : ListUser) {
                            appendToPane(jtextListUsers, user);
                        }
                    }
                    else{
                        appendToPane(jtextFilDiscu, message);
                    }
                }
            }
            catch (IOException ex) {
            }
            }
        }
    }

    //Send html to pane
    private void appendToPane(JTextPane tp, String msg){
        HTMLDocument doc = (HTMLDocument)tp.getDocument();
        HTMLEditorKit editorKit = (HTMLEditorKit)tp.getEditorKit();
        try {
            editorKit.insertHTML(doc, doc.getLength(), msg, 0, 0, null);
            tp.setCaretPosition(doc.getLength());
        } 
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
