<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Integrate with FB</title>
</head>
<body>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
	<script>
	  window.fbAsyncInit = function() {
	    FB.init({
	      appId      : '521073994746729',
	      xfbml      : true,
	      version    : 'v2.6'
	    });
	  };
	var pageId;
	  (function(d, s, id){
	     var js, fjs = d.getElementsByTagName(s)[0];
	     if (d.getElementById(id)) {return;}
	     js = d.createElement(s); js.id = id;
	     js.src = "//connect.facebook.net/en_US/sdk.js";
	     fjs.parentNode.insertBefore(js, fjs);
	   }(document, 'script', 'facebook-jssdk'));

		function subscribeApp(page_id, page_access_token) {
			console.log("Subscribing to the page : " + page_id);
			FB.api('/' + page_id + '/subscribed_apps', 'post', {
				access_token : page_access_token
			}, function(response) {
				//Convert the short-lived access_token to long-lived
				/* How to generate a long-lived token:
					1.Start with a short-lived token generated on a client and ship it back to your server.
					2.Use the user token, your app ID and app secret to make the following call from your server to Facebook's servers: */
				submitPageId(page_id,page_access_token);
				console.log("2.Subscribing to the page", response);
			});
		}

		function submitPageId(page_id,page_access_token) {
			console.log("1.Coming here", page_id);
			$.ajax({
				url : 'Subscription',
				data : {"page_id":page_id, "access_token":page_access_token},
				type : 'post',
				success : function(data) {
					console.log(data);
				}
			});
		}

		// Only works after `FB.init` is called
		function myFacebookLogin() {
			FB.login(function(response) {
				console.log("Successfully Loggedin", response);
				FB.api('/me/accounts', function(response) {
					console.log("Successfully retireved pages", response);
					var pages = response.data;
					var ul = document.getElementById("list");
					for (var i = 0; len = pages.length; i++) {
						var page = pages[i];
						var li = document.createElement('li');
						var a = document.createElement('a');
						a.href = "#";
						a.onclick = subscribeApp.bind(this, page.id, page.access_token);
						a.innerHTML = page.name;
						li.appendChild(a);
						ul.appendChild(li);
					}
				});
			}, {
				scope : 'manage_pages'
			});
		}
	</script>
	<button onclick="myFacebookLogin()">Login with Facebook</button>
	<ul id="list"></ul>
</body>
</html>