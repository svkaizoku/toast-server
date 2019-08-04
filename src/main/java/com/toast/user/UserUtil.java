package com.toast.user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.json.JSONObject;


public class UserUtil
{
	private static final String USER_INSERT = "INSERT INTO user (first_name, last_tname, username) VALUES (?,?,?)";
	private static final String AUTH_INSERT = "INSERT INTO auth (id, oauth) VALUES (?,?)";
	
	public Integer persistUser(JSONObject object)
	{
		if(isValid(object))
		{
			String username = object.getString("username");
			String firstName = object.getString("first_name");
			String lastName = object.getString("last_name");
			String oauthToken = object.getString("oauth_token");
			try (Connection conn = DriverManager.getConnection(
					"jdbc:mysql://localhost/toast", "root", "");
	             PreparedStatement preparedStatement = conn.prepareStatement(USER_INSERT)) {

	            preparedStatement.setString(1, firstName);
	            preparedStatement.setString(2, lastName);
	            preparedStatement.setString(3, username);
	            int row = preparedStatement.executeUpdate();
	            ResultSet rs = preparedStatement.getGeneratedKeys();
	            Integer userID = rs.getInt(0);
	            // rows affected
	            System.out.println(row);
	            persistOAuthToken(userID, oauthToken);
	            return userID;

	        } catch (SQLException e) {
	            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			
		}
		else if(object.has("oauth_token"))
		{
			Integer userID = null;
			String oAuthToken = null;
			if(object.has("user_id"))
			{
				userID = object.getInt("user_id");
				if(object.has("oauth_token"))
				{
					oAuthToken = object.getString("oauth_token");
					persistOAuthToken(userID, oAuthToken);
					
				}
			}
			return userID;
		}
		return null;
	}
	
	public void persistOAuthToken(Integer userId, String OAuthToken)
	{
		try (Connection conn = DriverManager.getConnection(
				"jdbc:mysql://localhost/mickeylite/toast", "root", "");
             PreparedStatement preparedStatement = conn.prepareStatement(AUTH_INSERT)) {

            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, OAuthToken);
            int row = preparedStatement.executeUpdate();
            System.out.println(row);

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private boolean isValid(JSONObject object)
	{
		String[] a = {"username", "first_name", "last_name", "oauth_token"};
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
