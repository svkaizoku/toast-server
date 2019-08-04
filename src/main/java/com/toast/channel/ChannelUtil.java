package com.toast.channel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChannelUtil
{
	private static final String CHANNEL_INSERT = "INSERT INTO channel (name, description, start_time) VALUES (?,?,?)";
	private static final String CHANNEL_USER_SELECT = "SELECT first_name, last_name, id  FROM users WHERE id IN (SELECT  user_id FROM channel_members WHERE channel_id= ?)";
	private static final String CHANNEL_SELECT = "SELECT * FROM channel WHERE id = ?";
	
	public JSONObject createChannel(JSONObject jsonObject)
	{
		Integer channelID = null;
		JSONObject returnJSON = new JSONObject();
		Timestamp timeStamp = null;
		if(isValidChannel(returnJSON))
		{
			String name = returnJSON.getString("name");
			String description = returnJSON.getString("description");
			
			try{
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				DriverManager.registerDriver(new com.mysql.jdbc.Driver());
				
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/toast", "root", "");
	            PreparedStatement preparedStatement = conn.prepareStatement(CHANNEL_INSERT,Statement.RETURN_GENERATED_KEYS);

	            preparedStatement.setString(1, name);
	            preparedStatement.setString(2, description);
	            timeStamp = new Timestamp(System.currentTimeMillis());
	            preparedStatement.setTimestamp(3, timeStamp);
	            
	            preparedStatement.executeUpdate();
	            ResultSet rs = preparedStatement.getGeneratedKeys();
	            channelID = rs.getInt(1);
	            returnJSON.put("channel_id", channelID);
	            returnJSON.put("start_time", timeStamp);

	        } catch (SQLException e) {
	            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

		}
		return returnJSON;
	}
	
	public JSONObject getChannel(Integer channelId)
	{
		JSONObject channelObject = new JSONObject();
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/toast", "root", "");
			PreparedStatement preparedStatement = conn.prepareStatement(CHANNEL_SELECT);

            preparedStatement.setInt(1, channelId);
            
            int row = preparedStatement.executeUpdate();
            ResultSet rs = preparedStatement.getResultSet();
            while(rs.next())
            {
            	String name = rs.getString("name");
            	String description = rs.getString("description");
            	Timestamp start_time = rs.getTimestamp("start_time");
            	channelObject.put("channel_id", channelId);
            	channelObject.put("name", name);
            	channelObject.put("description", description);
            	channelObject.put("start_time", String.valueOf(start_time));
            	long milli_diff = (System.currentTimeMillis() - start_time.getTime());
            	if(milli_diff > 300000)
            	{
            		channelObject.put("is_active", false);
            	}
            	else
            	{
            		channelObject.put("is_active", true);
            	}
            	
            	return channelObject;
            		
            }

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	
	public JSONArray getUsersInChannel(Integer channelId)
	{
			try{
				JSONArray usersArray = new JSONArray();
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				DriverManager.registerDriver(new com.mysql.jdbc.Driver());
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/toast", "root", "");
	            PreparedStatement preparedStatement = conn.prepareStatement(CHANNEL_USER_SELECT);
	
	            preparedStatement.setInt(1, channelId);
	            
	            int row = preparedStatement.executeUpdate();
	            ResultSet rs = preparedStatement.getResultSet();
	            while(rs.next())
	            {
	            	JSONObject channelObject =  new JSONObject();
	            	String first_name = rs.getString("first_name");
	            	String last_name = rs.getString("last_name");
	            	Integer id = rs.getInt("id");
	            	channelObject.put("first_name", first_name);
	            	channelObject.put("last_name", last_name);
	            	channelObject.put("id", id);
	            	
	            	usersArray.put(channelObject);
	            }
	
	        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
		return null;
	}
	private boolean isValidChannel(JSONObject object)
	{
		String[] a = {"name", "description"};
		List<String> necessaryKeys = Arrays.asList(a);
		Set<String> keySet = object.keySet();
		for(String necessaryKey : necessaryKeys)
		{
			if(!keySet.contains(necessaryKey))
			{
				return false;
			}
		}
		
		return true;
	}


}
