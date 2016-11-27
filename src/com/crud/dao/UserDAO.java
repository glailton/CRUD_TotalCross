package com.crud.dao;

import com.crud.model.User;

import litebase.LitebaseConnection;
import litebase.PreparedStatement;
import litebase.ResultSet;
import totalcross.sql.Connection;

public class UserDAO {
	
	private LitebaseConnection connection = LitebaseConnection.getInstance("APPI");
	private PreparedStatement prepStmtInsert;
	private PreparedStatement prepStmtUpdate;
	private PreparedStatement prepStmtDelete;
	private PreparedStatement prepStmtSelect;
	private PreparedStatement prepStmtId;

	public UserDAO() {
		
		if(!connection.exists("user")){
			connection.execute("create table user "
					+ "(id int primary key not null,"
					+ "name char(100) not null,"
					+ "age int not null)");
		}
	}
	
	public void insert(User user){
		
		prepStmtInsert = connection.prepareStatement(" insert into user values (?, ?, ?) ");
		
		user.setId(nextId());
		
		prepStmtInsert.clearParameters();
		prepStmtInsert.setInt(0, user.getId());
		prepStmtInsert.setString(1, user.getName());
		prepStmtInsert.setInt(2, user.getAge());
		
		prepStmtInsert.executeUpdate();
	}
	
	public void update(User user){
		
		prepStmtUpdate = connection.prepareStatement(" update user set name = ?, age = ? where id = ? ");
		
		prepStmtUpdate.clearParameters();
		prepStmtUpdate.setString(0, user.getName());
		prepStmtUpdate.setInt(1, user.getAge());
		prepStmtUpdate.setInt(2, user.getId());
		
		
		prepStmtUpdate.executeUpdate();
	}
	
	public void delete(User user){
		
		prepStmtDelete = connection.prepareStatement(" delete user where id = ? ");
		
		prepStmtDelete.clearParameters();
		prepStmtDelete.setInt(0, user.getId());
		
		prepStmtDelete.executeUpdate();
	}
	
	public String [] [] listAll(){
		
		String [] [] res = null;
	
		prepStmtSelect = connection.prepareStatement(" select * from user ");
		
		ResultSet result = prepStmtSelect.executeQuery();
		
		if(result.next()){
			result.first();
			res = result.getStrings();
		}
		
		result.close();
		
		return res;
	}
	
	private int nextId(){
		
		prepStmtId = connection.prepareStatement(" select max(id) as vId from user ");
		
		int res = 1;
		
		ResultSet result = prepStmtId.executeQuery();
		result.beforeFirst();
		
		while(result.next()){
			res = result.getInt("vId")+1;
		}
		result.close();
		
		return res;
	}
}
