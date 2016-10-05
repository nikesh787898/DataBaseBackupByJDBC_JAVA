package com.matrix.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.*;
class DatabaseBackUp
{
	private Connection con;
	private boolean log;
	private File outputFile;
	public DatabaseBackUp(Connection con,File outputFile,boolean log) throws Exception
	{	
		if(con==null)
                   throw new Exception("Invaild Argument : Connection can't be null.");
                this.con = con;
		this.outputFile = outputFile;
		this.log = log;
	}
        public void backup() throws FileNotFoundException, SQLException
        {
			List<String> tables = getAllTables();
			FileOutputStream outStream = new FileOutputStream(outputFile);
			PrintWriter pw = new PrintWriter(outStream);
			for(int i = 0 ; i<tables.size() ; i++ )
			{
				List<String> columns = new ArrayList<>();
				String table = tables.get(i);
				HashMap< String , Integer > map = getAllColumn(table);
				Statement statement = con.createStatement();
	    			ResultSet rs = statement.executeQuery("SELECT * FROM "+table);
				Iterator <String> iter = map.keySet().iterator();
				String queryHeader = "insert into "+ table  + " ( " ;
				while(iter.hasNext())
				{
					queryHeader += iter.next();
					if(iter.hasNext())
						queryHeader += " , "; 				
				}
				queryHeader += " ) values  ";
				
				int count =0;
				while (rs.next()) 
				{	
					String queryRecord = "";
					if(count > 0)
						queryRecord += ",";
					queryRecord += " ( ";					
							
					Iterator <String> iterCol = map.keySet().iterator();
					while(iterCol.hasNext())
					{
						String colName = iterCol.next();
						String value = rs.getString(colName);
						int colType = map.get(colName);	
					 	if(colType ==  Types.VARCHAR || colType ==  Types.CHAR || colType ==  Types.DATE )
					 		queryRecord += "'" + value +"'" ;
					 	else
					 		queryRecord +=  value  ;
						if(iterCol.hasNext())
							queryRecord += " , "; 			
					}
					queryRecord += " )";
					count ++;
					queryHeader += queryRecord;	 
				}
				queryHeader += " ; ";
				System.out.println(queryHeader);
				pw.println(queryHeader);
			}	
			pw.close();
    	}
	private HashMap<String,Integer> getAllColumn(String table) throws SQLException
	{
		if(log)
		{
			System.out.println("Getting list of the all Columns for table "+table+"...");
		}
		Statement statement = con.createStatement();
    		ResultSet resultSet = statement.executeQuery("SELECT * FROM "+table);

    		ResultSetMetaData metadata = resultSet.getMetaData();
		int columnCount = metadata.getColumnCount();

		HashMap<String, Integer> map = new HashMap<>();
		for (int i = 1; i <= columnCount; i++) 
		{
			 String columnName = metadata.getColumnName(i);
		     int columnType = metadata.getColumnType(i) ;
		     	 if(log)
			 {
				System.out.println(columnName + " " + columnType );
			 }
		      	 map.put(columnName, columnType);
		}
		return map;
	}
	private List<String> getAllTables() throws SQLException
	{
		List<String> list = new ArrayList<String>();
		DatabaseMetaData md = con.getMetaData();
		ResultSet rs = md.getTables(null, null, "%", null);
		if(log)
		{
			System.out.println("Getting list of the all tables...");
		}
		while (rs.next()) 
		{
			String table = rs.getString(3);
			if(log)
			{
				System.out.println(table);
			}
			list.add(table);
		}
		return list;
	}
}