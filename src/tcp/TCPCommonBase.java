/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Diego
 */
public abstract class TCPCommonBase implements Runnable {
    protected String TAG = "TPCCommonBase";
    protected DataOutputStream out;
    protected BufferedReader in;
    protected Socket socket;
    protected int port;
    protected boolean isOn;
    protected ReceiveListener receiveListenner;
    
    public abstract void init();
    
    public void setReceiveListener(ReceiveListener listener) {
        receiveListenner = listener;
    }
    
    public void setTag(String tag) {
        TAG = tag;
    }
    
    public void send(String msg) throws IOException {
        out.writeBytes(msg);
    }

    public void stop() {
        isOn = false;
    }

    protected void receive(String message) {
        Matcher matcher = Pattern.compile("^server(\\s.*)*").matcher(message);
        if(matcher.find()) {
            String param = matcher.group(1);
            if(param.equals("stop"))
                stop();
        }
        
        if(receiveListenner != null)
            receiveListenner.onReceive(message);
    }
    
    @Override
    public void run() {
        try {
            init();
            System.out.println("Server " + TAG + " running...");
            
            while(isOn) {
                System.out.println("Server " + TAG + " waiting...");
                String message = in.readLine();
                System.out.println("Server " + TAG + " received...");
                receive(message);
            }
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
