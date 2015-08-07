/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Executor;

/**
 *
 * @author Diego
 */
public class Main {
    
    /**
     * @param args the command line arguments
     * @throws java.lang.InterruptedException
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws InterruptedException, IOException {
        int port = 6789;
        String host = "192.168.42.7";
        Executor ex = new Executor() {
            @Override
            public void execute(Runnable command) {
                new Thread(command).start();
            }
        };
        
        TCPClient client = new TCPClient(host, port);
        ex.execute(client);
        
        System.out.println("Send `server stop` to exit!\n\n");
        
        while(true) {
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
            String msg = inFromUser.readLine();
            
            if(msg.equals("server stop"))
                break;
            
            client.send(msg + "\n");
        }
    }
    
}
