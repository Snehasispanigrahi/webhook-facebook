<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Integrate with FB</title>
</head>
<body>
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
	function myFacebookLogin() {FB.api(
			  '/130985787307645',
			  'GET',
			  {},
			  function(response) {
				  console.log("Successful", response);
			  }
			);}
	</script>
	<button onclick="myFacebookLogin()">Login with Facebook</button>
</body>
</html>