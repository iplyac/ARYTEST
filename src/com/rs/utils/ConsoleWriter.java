/***************************************************************************
 * 5724-H93
 * (c) Copyright Rocket Software, Inc. 2002 - 2009 All Rights Reserved.
 *
 * $Id: ConsoleWriter.java,v 1.4 2010/11/19 22:36:00 pstewart Exp $
 ****************************************************************************/

package com.rs.utils;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * This class is wrapper for <code>System.out</code>.
 * 
 * @author amarkelov
 */
public class ConsoleWriter
{

    /**
     * Printer to console.
     */
    private static PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));

    /**
     * Private constructor.
     */
    private ConsoleWriter() {}

    /**
     * Init <code>java.io.PrintWriter</code> with <code>System.out</code>.
     */
    public static void init()
    {
//        out = new PrintWriter(new OutputStreamWriter(System.out));
    }

    /**
     * Set encoding to system out stream.
     */
    public static void init(String encoding)
    throws UnsupportedEncodingException
    {
        out = new PrintWriter(new OutputStreamWriter(System.out, encoding));
    }


    public static void print(int line)
    {
        out.print(line);
        out.flush();
    }
    
    public static void print(StringBuffer line)
    {
        out.print(line);
        out.flush();
    }
    
    public static void print(char line)
    {
        out.print(line);
        out.flush();
    }
    
    /**
     * Write line.
     */
    public static void print(String line)
    {
        out.print(line);
        out.flush();
    }

    /**
     * Write line.
     */
    public static void println(String line)
    {
        out.println(line);
        out.flush();
    }
    
    public static void println(int line)
    {
        out.println(line);
        out.flush();
    }
    
    /**
     * Write empty line.
     */
    public static void println()
    {
        out.println();
        out.flush();
    }
}
