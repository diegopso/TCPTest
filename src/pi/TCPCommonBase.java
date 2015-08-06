/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
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
    protected ReceiveListener receiveListenner;
    
    public abstract void init();
    
    public void setReceiveListener(ReceiveListener listener) {
        receiveListenner = listener;
    }
    
    public void setTag(String tag) {
        TAG = tag;
    }
    
    public void stop() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(TCPCommonBase.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void send(String msg) throws IOException {
        out.writeBytes(msg);
    }

    protected void receive(String message) {
        if(message == null)
            return;
        
        Matcher matcher = Pattern.compile("^server(\\s.*)?").matcher(message);
        if(matcher.find()) {
            String param = matcher.group(1).trim();
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
            
            while(true) {
                String message = in.readLine();
                receive(message);
            }
        } catch (SocketException ex) {
            if(!ex.getMessage().equals("socket closed"))
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
}
