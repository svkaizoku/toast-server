package com.toast.channel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;

public class ChannelUtil
{
	private static final String CHANNEL_INSERT = "INSERT INTO user (name, description, start_time, is_active) VALUES (?,?,?,?)";
	
	public JSONObject createChannel(JSONObject jsonObject)
	{
		Integer channelID = null;
		JSONObject returnJSON = new JSONObject();
		Timestamp timeStamp = null;
		if(isValidChannel(returnJSON))
		{
			String name = returnJSON.getString("name");
			String description = returnJSON.getString("description");
			
			try (Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/toast", "root", "root");
	             PreparedStatement preparedStatement = conn.prepareStatement(CHANNEL_INSERT)) {

	            preparedStatement.setString(1, name);
	            preparedStatement.setString(2, description);
	            timeStamp = new Timestamp(System.currentTimeMillis());
	            preparedStatement.setTimestamp(3, timeStamp);
	            preparedStatement.setBoolean(4, true);
	            
	            int row = preparedStatement.executeUpdate();
	            ResultSet rs = preparedStatement.getGeneratedKeys();
	            channelID = rs.getInt(0);
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
