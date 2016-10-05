package com.matrix.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseRestore {
	private Connection connection;
	private File file;
	public DatabaseRestore(Connection connection,File file)
	{
		this.connection = connection;
		this.file = file;
	}
	public void restore() throws SQLException, IOException
	{
		Statement statement = connection.createStatement();
		FileInputStream fileInputStream =new FileInputStream(file);
		InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String line = "";
		while((line = bufferedReader.readLine())!=null)
		{
			System.out.println(line);
			try
			{
				statement.executeUpdate(line);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
		statement.close();
		bufferedReader.close();
	}
	public static void main(String[] args) {
		

	}

}
