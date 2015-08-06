/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tcp;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Diego
 */
public class PiGlassServer implements Runnable {
    private final TCPServer glass, local;
    
    public PiGlassServer(int glassPort, int localPort) {
        glass = new TCPServer(glassPort);
        glass.setTag("Glass Server");
        glass.setReceiveListener(new ReceiveListener() {
            @Override
            public void onReceive(String msg) {
                receiveFromGlass(msg);
            }
        });
        
        local = new TCPServer(localPort);
        local.setTag("Local Server");
        local.setReceiveListener(new ReceiveListener() {
            @Override
            public void onReceive(String msg) {
                receiveLocaly(msg);
            }
        });
    }
    
    public void receiveFromGlass(String msg) {
        beforeReceive(msg);
    }
    
    public void receiveLocaly(String msg) {
        beforeReceive(msg);
        
        try {
            glass.send(msg);
        } catch (IOException ex) {
            Logger.getLogger(PiGlassServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        Executor ex = new Executor() {
            @Override
            public void execute(Runnable command) {
                new Thread(command).start();
            }
        };
        
        ex.execute(glass);
        ex.execute(local);
    }

    private void beforeReceive(String msg) {
        Matcher matcher = Pattern.compile("^server(\\s.*)*").matcher(msg);
        if(matcher.find()) {
            String param = matcher.group(1);
            if(param.equals("stop"))
                stop();
        }
    }

    private void stop() {
        local.stop();
        glass.stop();
    }
}
