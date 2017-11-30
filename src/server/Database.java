package server;
import java.sql.*;
import java.util.*;

import util.GameState;
public class Database {
	private static Connection db;
	private static void stupidCheck()
	{
		query("CREATE SCHEMA if not exists `checkers`", null);
		query("CREATE TABLE if not exists checkers.users (`password` varchar(64), `username` varchar(20),"
		+"`wins` int default 0, `losses` int default 0, `ties` int default 0, primary key (`username`))", null);
	}
	private static void connect()
	{
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			db = DriverManager.getConnection("jdbc:mysql://localhost:3306?useSSL=false","root","");
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
			if(parameters!=null)
			{
				for(int i=0;i<parameters.size();i++)
				{
					pQuery.setString(i+1,parameters.get(i));
				}
			}
			if(query.charAt(0)=='S')
			{
				dataSet = pQuery.executeQuery();
				dataSet.next();
			}else
			{
				pQuery.executeUpdate();
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return dataSet;
	}
	public static ArrayList<String> login(String user, String pass)
	{
		stupidCheck();
		String query;
		ResultSet dataSet = null;
		ArrayList<String> data = new ArrayList<String>();
		ArrayList<String> parameters = new ArrayList<String>();
		
		if(user.length()>20 || pass.length()>64)
			return data;
		
		parameters.add(user);
		query = "SELECT count(*) from checkers.users where username=?;";
		dataSet = query(query, parameters);
		parameters.add(pass);
		try {
			if(dataSet.getString("count(*)").equals("0"))
			{
				query = "INSERT INTO checkers.users(`username`, `password`) VALUES (?, ?);";
				dataSet = query(query, parameters);
				
			}
			query = "SELECT wins, losses, ties from checkers.users where username=? and password=?;";
			dataSet = query(query, parameters);
			try {
				if(dataSet.first())
				{
					data.add("1");
					data.add(dataSet.getString("wins"));
					data.add(dataSet.getString("losses"));
					data.add(dataSet.getString("ties"));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (NumberFormatException | SQLException e) {
			e.printStackTrace();
		}
		try {
			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	public static void updateScores(GameState g)
	{
		stupidCheck();
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
		try {
			db.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}