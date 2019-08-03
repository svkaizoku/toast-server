package com.toast.customdatastructure;

import java.util.HashMap;

public class OrderedFixedHashMap extends HashMap<String, Integer>
{
	
	private static final long serialVersionUID = -5345858814688468542L;
	private String minKey = null;
	private Integer minValue = null;
	private Integer maxValue = null;
	private int maxSize;


	public OrderedFixedHashMap(int n) {
		this.maxSize = n;
	}
	
	@Override
	public Integer put(String key, Integer value)
	{
		if(this.size() < maxSize)
		{
			if(minValue == null || minValue > value)
			{
				minValue = value;
				minKey = key;
			}
			if(maxValue == null || maxValue < value)
			{
				maxValue = value;
			}
			 return super.put(key, value);
		}
		else
		{
			if(value > minValue)
			{
				super.remove(minKey);
				minKey = null;
				minValue = null;
				if(maxValue == null || maxValue < value)
				{
					maxValue = value;
				}
				Integer returnValue = super.put(key, value);
				recomputeMin();
				return returnValue;
			}
		}
		return null;
	}

	
	private void recomputeMin()
	{
		for(String key : super.keySet())
		{
			Integer value = super.get(key);
			if(minValue == null || minValue > value)
			{
				minValue = value;
				minKey = key;
			}
		}
	}
	
	
}
