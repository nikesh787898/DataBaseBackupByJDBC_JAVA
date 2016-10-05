package com.matrix.main;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main2 {
	
	public static void main(String[] args) throws Exception {
		DriverManager.registerDriver(new com.mysql.jdbc.Driver());
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dbback","root","");
		DatabaseRestore databaseRestore = new DatabaseRestore(connection, new File("backup.txt"));
		databaseRestore.restore();

	}
}
