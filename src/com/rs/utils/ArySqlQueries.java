package com.rs.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * The wrapper on file which contains ARY SQL queries.
 * 
 * @author amarkelov
 */
public class ArySqlQueries
{
    /**
     * It is file which contains ARY SQL queries. 
     */
    private static final String SQL_FILE = "sql_queries.arysql";

    /**
     * Properties static instance.
     */
    private static Properties queries; 

    /**
     * Get SQL by key.
     */
    public static String getQuery(String sqlKey)
    {
    	init();
        return queries.getProperty(sqlKey);
    }

    /**
     * Initialize properties instance. Read all SQl from file.
     */
    public static void init()
    {
        InputStream inStream = null;
        try {
            inStream = ArySqlQueries.class.getResourceAsStream(SQL_FILE);
            queries = new Properties();
            queries.load(inStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
    }

    /**
     * Private constructor.
     */
    private ArySqlQueries() {}
}
