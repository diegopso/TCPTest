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
        int portGlass = 6789;
        int portLocal = 6788;
        String host = "localhost";
        Executor ex = new Executor() {
            @Override
            public void execute(Runnable command) {
                new Thread(command).start();
            }
        };
        
        PiGlassServer server = new PiGlassServer(portGlass, portLocal);
        server.run();
        
        TCPClient glass = new TCPClient(host, portGlass);
        glass.setTag("Glass Client");
        glass.setReceiveListener(new ReceiveListener() {
            @Override
            public void onReceive(String msg) {
                System.out.println("Glass received: " + msg);
            }
        });
        ex.execute(glass);
        
        TCPClient local = new TCPClient(host, portLocal);
        local.setTag("Local Client");
        ex.execute(local);
        
        // ensure theyre connected
        Thread.sleep(2000);
        
        int count = 0;
        String[] msgs = new String[] {"ola", "glass", "bye", "glass", "server stop"};
        while(count < 5) {
            local.send(msgs[count]);
            Thread.sleep(1000);
            count++;
        }
        
        glass.stop();
        local.stop();
    }
    
}
