/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rm5248.testingserial;

import com.rm5248.serial.NoSuchPortException;
import com.rm5248.serial.NotASerialPortException;
import com.rm5248.serial.SerialLineState;
import com.rm5248.serial.SerialPort;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Rob
 */
public class SerialTest {
    
    private static SerialPort port1;
    private static SerialPort port2;
    
    public static void main( String[] args ) throws IOException {
        System.out.println( "OS arch: " + System.getProperty( "os.arch" ) );
        System.out.println( "Loading serial port" );
        System.out.println( "Java version: " + SerialPort.getMajorVersion() + "." + SerialPort.getMinorVersion() );
        System.out.println( "Native code version: " + SerialPort.getMajorNativeVersion() + "." + SerialPort.getMinorNativeVersion() );
        
        System.out.println( "All ports: " );
        String[] ports = SerialPort.getSerialPorts();
        for( int x = 0; x < ports.length; x++ ){
            System.out.println( "  " + ports[ x ] );
        }
        
        if( args.length <= 1 ){
            return;
        }
        
        if( args.length >= 2 ){
            try {
                port1 = new SerialPort( args[ 1 ] );
            } catch (NoSuchPortException ex) {
                ex.printStackTrace();
                return;
            } catch (NotASerialPortException ex) {
                ex.printStackTrace();
                return;
            }
        }
        
        if( args.length >= 3 ){
            try {
                port2 = new SerialPort( args[ 2 ] );
            } catch (NoSuchPortException ex) {
                ex.printStackTrace();
                return;
            } catch (NotASerialPortException ex) {
                ex.printStackTrace();
                return;
            }
        }
        
        if( args[ 0 ].equals( "test-states" ) ){
            checkStates();
        }

        if( args[ 0 ].equals( "test-tx" ) ){
            testTx();
        }
        
        if( args[ 0 ].equals( "test-rx" ) ){
            testRx();
        }
        
        port1.close();
        if( port2 != null ){
            port2.close();
        }
    }
    
    private static void testTx() throws IOException {
        port1.getOutputStream().write( "This is a test string".getBytes() );
    }
    
    private static void testRx() throws IOException {
        BufferedReader reader = new BufferedReader( new InputStreamReader( port1.getInputStream() ) );
        System.out.println( "Got line: " + reader.readLine() );
    }
    
    private static void checkStates(){        
        try{
            SerialLineState port1State = port1.getSerialLineState();
            SerialLineState port2State = port2.getSerialLineState();
            
            for( int x = 0; x < 10000000; x++ ){
                SerialLineState port1new = port1.getSerialLineState();
                SerialLineState port2new = port2.getSerialLineState();

                if( !port1new.equals( port1State ) ){
                    System.out.println( "New state port1: " + port1new );
                }

                if( !port2new.equals( port2State ) ){
                    System.out.println( "New state port2: " + port2new );
                }

                port1State = port1new;
                port2State = port2new;
                
                if( x % 100000 == 0 ){
                    System.out.println( "Time " + x );
                }
                
                Thread.sleep( 10 );
            }
            
        }catch( IOException | InterruptedException ex ){
            
        } 
    }
    
    private static void testInterrupt(){
        Thread thisThread = Thread.currentThread();
        Interruptor i;
        //i = new Interruptor( thisThread );
        i = new Interruptor( port1 );
        Thread t2 = new Thread( i );
        t2.start();
        
        int val;
        try {
            val = port1.getInputStream().read();
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        System.out.println( "got " + val );
    }
    
}
