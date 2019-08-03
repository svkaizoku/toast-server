package com.toast.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Reader;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.IOUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import com.toast.utils.MessageStatUtil;

/**
 * Servlet implementation class MessageStats
 */
public class MessageStatServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponseresponse)
	 *      
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Reader reader = request.getReader();
		out.append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponseresponse)
	 * 
	 */
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		PrintWriter out = response.getWriter();
		try {
			JSONArray input = processInput(request);
			MessageStatUtil msgStatUtil = new MessageStatUtil();
			JSONArray result = msgStatUtil.crunchNumbers(input);
			out.append(result.toString());
		}
		catch (FileUploadException e) {
			System.err.println("Error while processing input form request " + request);
			e.printStackTrace();
			throw new ServletException(e);
		}

	}

	/**
	 * Process input from {@link HttpServletRequest} for different contentTypes
	 * 
	 * @param req
	 * @return jsonArray
	 * @throws IOException
	 * @throws FileUploadException
	 * @throws ServletException
	 */
	private JSONArray processInput(HttpServletRequest req) throws IOException, FileUploadException, ServletException {
		String data;
		
		if (req.getContentType().contains("multipart/form-data;")) {
			Part part = req.getPart("data");
			data = IOUtils.toString(part.getInputStream());
		}
		else if (req.getContentType().contains("application/json")) {
			String jsonData = IOUtils.toString(req.getReader());
			JSONObject jsonObject = new JSONObject(jsonData);
			data = jsonObject.get("data").toString();
		}
		else {
			data = req.getParameter("data");
		}
		JSONArray JSONdata = new JSONArray(data);
		return JSONdata;
	}

}
