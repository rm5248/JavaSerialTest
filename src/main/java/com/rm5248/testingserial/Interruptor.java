/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm5248.testingserial;

import com.rm5248.serial.SerialPort;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rob
 */
public class Interruptor implements Runnable{
    
    private Thread toInterrupt;
    private SerialPort toClose;
    
    public Interruptor( Thread toInterrupt ){
        this.toInterrupt = toInterrupt;
    }
    
    public Interruptor( SerialPort toClose ){
        this.toClose = toClose;
    }

    @Override
    public void run() {
        try {
            System.out.println( "Running" );
            Thread.sleep( 500 );
            
            if( toInterrupt != null ){
                System.out.println( "About to interrupt" );
                toInterrupt.interrupt();
            }
            
            if( toClose != null ){
                System.out.println( "About to close" );
                toClose.close();
            }
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    
}
