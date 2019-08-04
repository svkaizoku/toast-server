package com.toast.channel;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileUploadException;
import org.json.JSONObject;

import com.toast.Misc;


/**
 * Servlet implementation class MessageStats
 */
public class ChannelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponseresponse)
	 *      
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Reader reader = request.getReader();
		out.append("Served at: ").append(request.getRequestURI());
		String reqURI = request.getRequestURI();
		List url = Arrays.asList(reqURI.split("//"));
		ChannelUtil chUtil = new ChannelUtil();
		if(url.size() == 3)
		{
			
			Integer channelID = new Integer((int) url.get(2));
			JSONObject returnJSON = chUtil.getChannel(channelID);
			out.print(returnJSON.toString());
		}
		
		if(url.size() == 5)
		{
			// get Stream/id/user
		}
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponseresponse)
	 * 
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		Misc misc = new Misc();
		try
		{
			String reqURI = request.getRequestURI();
			List url = Arrays.asList(reqURI.split("//"));
			if(url.size() == 2)
			{
				JSONObject input = misc.processInput(request);
				ChannelUtil chUtil = new ChannelUtil();
				JSONObject returnJSON = chUtil.createChannel(input);
				out.print(returnJSON.toString());
			}
			
			if(url.size() == 4)
			{
				// add users to Stream
			}
			
			
		}
		catch (FileUploadException e)
		{
			e.printStackTrace();
		}
		
	}

	


}
