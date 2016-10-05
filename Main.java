package com.matrix.main;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main {
	public static void main(String args[]) throws Exception
	{
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/matrixims","root","");
		DatabaseBackUp backUp = new DatabaseBackUp(connection, new File("backup.txt"), true);
		backUp.backup();
		
	}
}
