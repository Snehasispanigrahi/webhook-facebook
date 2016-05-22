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