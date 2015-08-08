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
public class Send {
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
        client.init();
        
        for (int i = 0; i < args.length; i++) {
            client.send(args[i] + "\n");
            System.out.println(String.format("Message [%d] sent", i));
        }
        
        client.stop();
    }
}
