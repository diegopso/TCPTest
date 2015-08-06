/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tcp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Diego
 */
public class TCPServer extends TCPCommonBase {
    protected static String TAG = "TCPServer";
    private ServerSocket welcomeSocket;
    
    public TCPServer(int port) {
        this.port = port;
    }

    @Override
    public void init() {
        try {
            welcomeSocket = new ServerSocket(port);
            socket = welcomeSocket.accept();
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            Logger.getLogger(TCPServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
