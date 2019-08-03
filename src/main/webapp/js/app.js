function getStats(msgList) {
	var settings = {
		"async" : true,
		"url" : "/webapp/MessageStatServlet",
		"method" : "POST",
		"processData" : false,
		"contentType" : "application/json",
		"data" : JSON.stringify({
			"data" : msgList
		})
	}

	jQuery.ajax(settings).done(function(response) {
		console.log(response);
		jQuery('#message-count-table').bootstrapTable({data:JSON.parse(response)});
	});
}

function getMessageList() {
	jQuery.get("https://www.mocky.io/v2/5cdd110c3000007825e23470")
			.done(function(x) {
				getStats(x);
				messageList = x;
			}).fail(function() {
		alert("error");
	});
}

