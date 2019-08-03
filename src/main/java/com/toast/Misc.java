package com.toast;

import java.io.IOException;

//import io.agora.media.DynamicKey5;

import java.util.Date;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class Misc {
    

    public String genToken() throws Exception{
    	String appID = "";
        String appCertificate = "";
        String channelName = "";
        int ts = (int)(new Date().getTime()/1000);
        int r = new Random().nextInt();
        long uid = 0;
        int expiredTs = 0;
        //String token = DynamicKey5.generateMediaChannelKey(appID, appCertificate, channelName, ts, r, uid, expiredTs);
    	return null;
    }
    
    /**
	 * Process input from {@link HttpServletRequest} for different contentTypes
	 * 
	 * @param req
	 * @return JSONObject
	 * @throws IOException
	 * @throws FileUploadException
	 * @throws ServletException
	 */
    public JSONObject processInput(HttpServletRequest req) throws IOException, FileUploadException, ServletException {
		String data;
		
		if (req.getContentType().contains("multipart/form-data;")) {
			Part part = req.getPart("data");
			data = IOUtils.toString(part.getInputStream());
		}
		else if (req.getContentType().contains("application/json")) {
			String jsonData = IOUtils.toString(req.getReader());
			JSONObject jsonObject = new JSONObject(jsonData);
			return jsonObject;
		}
		else {
			data = req.getParameter("data");
			JSONObject JSONdata = new JSONObject(data);
			return JSONdata;
		}
		return null;
		

	}
}