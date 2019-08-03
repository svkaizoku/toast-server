package com.toast.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.toast.customdatastructure.OrderedFixedHashMap;

public class MessageStatUtil {

	public JSONArray crunchNumbers(JSONArray array) {
		String strObject;
		Map<String, String> userMessages = new HashMap<>();
		Map<String, Integer> userMessageCount = new HashMap<>();
		for (Object object : array) {
			if (object instanceof String) {
				strObject = (String) object;
				List<String> entry = Arrays.asList(strObject.split(":"));
				String name = entry.get(0);
				String message = entry.get(1);
				userMessages.put(name, message);
				if (userMessageCount.get(name) != null) {
					userMessageCount.put(name, userMessageCount.get(name) + 1);
				}
				else {
					userMessageCount.put(name, 1);
				}
			}
			else {

			}
		}
		com.toast.customdatastructure.OrderedFixedHashMap topMessagerMap = getTopMessagers(5, userMessageCount);
		JSONArray topMessagers = constructResponse(topMessagerMap);
		return topMessagers;
		

	}

	private OrderedFixedHashMap getTopMessagers(int n, Map<String, Integer> map) {
		OrderedFixedHashMap topMessagersMap = new OrderedFixedHashMap(n);
		for (String key : map.keySet()) {
			topMessagersMap.put(key, map.get(key));
		}
		return topMessagersMap;
	}
	
	private JSONArray constructResponse(OrderedFixedHashMap topMessagerMap)
	{
		JSONArray responseArray =  new JSONArray();
		JSONObject temp =  null;
		for(String key : topMessagerMap.keySet())
		{
			temp = new JSONObject();
			Integer value = topMessagerMap.get(key);
			temp.put("name", key);
			temp.put("messageCount", value);
			responseArray.put(temp);
			temp = null;
		}
		return responseArray;
	}
}
