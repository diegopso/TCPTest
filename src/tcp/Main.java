/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tcp;

import java.io.IOException;
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
        String host = "localhost";
        Executor ex = new Executor() {
            @Override
            public void execute(Runnable command) {
                new Thread(command).start();
            }
        };
        
        TCPServer server = new TCPServer(port);
        server.setReceiveListener(new ReceiveListener() {
            @Override
            public void onReceive(String msg) {
                System.out.println("Glass Received: " + msg);
            }
        });
        
        ex.execute(server);
        
        TCPClient client = new TCPClient(host, port);
        client.init();
        
        Thread.sleep(1000);
        
        client.send("hi\n");
        client.send("bye\n");
        client.send("server stop\n");
        client.stop();
    }
    
}
