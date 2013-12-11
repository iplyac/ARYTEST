package com.rs.utils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlUtil {

	public static void closeStatement(Statement stmt){
		try {
			stmt.close();
		}catch(SQLException ex){
    		System.err.println("SQLException information");
		      while(ex!=null) {
		        System.err.println ("Error msg: "  + ex.getMessage());
		        System.err.println ("SQLSTATE: "   + ex.getSQLState());
		        System.err.println ("Error code: " + ex.getErrorCode());
		        ex.printStackTrace();
		        ex = ex.getNextException(); 
				}
      }
	}
	
	
	public static void closeStatement(PreparedStatement stmt){
		try {
			stmt.close();
		}catch(SQLException ex){
    		System.err.println("SQLException information");
		      while(ex!=null) {
		        System.err.println ("Error msg: "  + ex.getMessage());
		        System.err.println ("SQLSTATE: "   + ex.getSQLState());
		        System.err.println ("Error code: " + ex.getErrorCode());
		        ex.printStackTrace();
		        ex = ex.getNextException(); 
				}
      }
	}
	
	
	public static void closeStatement(CallableStatement stmt){
		try {
			stmt.close();
		}catch(SQLException ex){
    		System.err.println("SQLException information");
		      while(ex!=null) {
		        System.err.println ("Error msg: "  + ex.getMessage());
		        System.err.println ("SQLSTATE: "   + ex.getSQLState());
		        System.err.println ("Error code: " + ex.getErrorCode());
		        ex.printStackTrace();
		        ex = ex.getNextException(); 
				}
      }
	}
	
	public static void closeResultSet(ResultSet rs){
		try {
			rs.close();
		}catch(SQLException ex){
    		System.err.println("SQLException information");
		      while(ex!=null) {
		        System.err.println ("Error msg: "  + ex.getMessage());
		        System.err.println ("SQLSTATE: "   + ex.getSQLState());
		        System.err.println ("Error code: " + ex.getErrorCode());
		        ex.printStackTrace();
		        ex = ex.getNextException(); 
				}
      }
	}
}
