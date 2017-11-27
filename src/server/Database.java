package server;
import java.sql.*;
import java.util.*;

import util.GameState;
public class Database {
	static Connection db;
	private static void connect()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			db = DriverManager.getConnection("jdbc:mysql://localhost:3306","root","");
			query("CREATE SCHEMA if not exists `checkers`", null);
			query("CREATE TABLE if not exists `users` (`password` varchar(20), `username` varchar(20),"
			+"`wins` int default 0, `losses` int default 0, `ties` int default 0, primary key (`username`))", null);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			System.out.println("Something broke");
		}
	}
	private static ResultSet query(String query, ArrayList<String> parameters)
	{
		ResultSet dataSet = null;
		PreparedStatement pQuery;
		connect();
		try {
			pQuery = db.prepareStatement(query);
			if(parameters.size()>0)
			{
				for(int i=1;i<parameters.size();i++)
				{
					pQuery.setString(i,parameters.get(i));
				}
			}
			dataSet = pQuery.executeQuery();
			dataSet.next();
			db.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dataSet;
	}
	public static ArrayList<String> login(String user, String pass)
	{
		String query;
		ResultSet dataSet = null;
		ArrayList<String> data = new ArrayList<String>();
		ArrayList<String> parameters = new ArrayList<String>();
		
		if(user.length()>20 || pass.length()>20)
			return data;
		
		parameters.add(user);
		query = "select count(*) from checkers.users where username=?;";
		dataSet = query(query, parameters);
		parameters.add(pass);
		try {
			if(dataSet.getFetchSize()>0 && Integer.parseInt(dataSet.getString("count(*)"))==0)
			{
				query = "INSERT INTO checkers.users(`username`, `password`) VALUES (?, ?);";
				dataSet = query(query, parameters);
				
			}
			query = "select username from checkers.users where username=? and password=?;";
			dataSet = query(query, parameters);
			try {
				if(dataSet.getFetchSize()>0) data.add(dataSet.getString("username"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
		return data;
	}
	public static void updateScores(GameState g)
	{
		String query = "UPDATE checkers.users SET wins=?, losses=?, ties=? where username=?";
		ResultSet dataSet = null;
		ArrayList<String> parameters = new ArrayList<String>();
		
		parameters.add(g.playerOneWins+"");
		parameters.add(g.playerOneLosses+"");
		parameters.add(g.playerOneTies+"");
		parameters.add(g.playerOneUserName);
		dataSet = query(query, parameters);
		
		parameters.add(g.playerTwoWins+"");
		parameters.add(g.playerTwoLosses+"");
		parameters.add(g.playerTwoTies+"");
		parameters.add(g.playerTwoUserName);
		dataSet = query(query, parameters);
	}
}