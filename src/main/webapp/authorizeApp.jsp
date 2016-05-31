<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Integrate with FB</title>
</head>
<body>
	<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/2.0.0/jquery.min.js"></script>
	<script>
		window.fbAsyncInit = function() {
		FB.init({
			appId		: '521073994746729',
		    cookie		: true,  // enable cookies to allow the server to access 
		    xfbml		: true,  // parse social plugins on this page
		    version		: 'v2.6' // use graph api version 2.6
		});
		// Now that we've initialized the JavaScript SDK, we call 
		  // FB.getLoginStatus().  This function gets the state of the
		  // person visiting this page and can return one of three states to
		  // the callback you provide.  They can be:
		  //
		  // 1. Logged into your app ('connected')
		  // 2. Logged into Facebook, but not your app ('not_authorized')
		  // 3. Not logged into Facebook and can't tell if they are logged into
		  //    your app or not.
		  //
		  // These three cases are handled in the callback function.

		  FB.getLoginStatus(function(response) {
		    statusChangeCallback(response);
		  });

	  };
	  
	// Load the SDK asynchronously
	  (function(d, s, id) {
	    var js, fjs = d.getElementsByTagName(s)[0];
	    if (d.getElementById(id)) return;
	    js = d.createElement(s); js.id = id;
	    js.src = "//connect.facebook.net/en_US/sdk.js";
	    fjs.parentNode.insertBefore(js, fjs);
	  }(document, 'script', 'facebook-jssdk'));
	
	
		var pageId;

		function subscribeApp(page_id, page_access_token) {
			console.log("Subscribing to the page : " + page_id);
			FB.api('/' + page_id + '/subscribed_apps', 'post', {
				access_token : page_access_token
			}, function(response) {
				//Convert the short-lived access_token to long-lived
				/* How to generate a long-lived token:
					1.Start with a short-lived token generated on a client and ship it back to your server.
					2.Use the user token, your app ID and app secret to make the following call from your server to Facebook's servers: */
				submitPageId(page_id, page_access_token);
				console.log("2.Subscribing to the page", response);
			});
		}

		function submitPageId(page_id, page_access_token) {
			console.log("1.Coming here", page_id, page_access_token);
			$.ajax({
				url : 'Subscription',
				data : {
					"page_id" : page_id,
					"access_token" : page_access_token
				},
				type : 'post',
				success : function(data) {
					console.log(data);
				}
			});
		}

		 // This is called with the results from from FB.getLoginStatus().
		  function statusChangeCallback(response) {
		    console.log('statusChangeCallback');
		    console.log(response);
		    // The response object is returned with a status field that lets the
		    // app know the current login status of the person.
		    // Full docs on the response object can be found in the documentation
		    // for FB.getLoginStatus().
		    if (response.status === 'connected') {
		      // Logged into your app and Facebook.
		      document.getElementById('status').innerHTML = "Connected";
		      getPages();
		    } else if (response.status === 'not_authorized') {
		      // The person is logged into Facebook, but not your app.
		      /* myFacebookLogin(); */
		      document.getElementById('status').innerHTML = 'Please log ' +
		        'into this app.';
		    } else {
		      // The person is not logged into Facebook, so we're not sure if
		      // they are logged into this app or not.
		      /* myFacebookLogin(); */
		      document.getElementById('status').innerHTML = 'Please log ' +
		        'into Facebook.';
		      
		    }
		  }
		 
		// This function is called when someone finishes with the Login
		  // Button.  See the onlogin handler attached to it in the sample
		  // code below.
		  function checkLoginState() {
		    FB.getLoginStatus(function(response) {
		      statusChangeCallback(response);
		    });
		  }
		
		
		// Here we run a very simple test of the Graph API after login is
		  // successful.  See statusChangeCallback() for when this call is made.
		  function getPages() {
		    console.log('Welcome!  Fetching your information.... ');
		    FB.api('/me/accounts', function(response) {
		      console.log('Successful login for: ' + response.name);
		      document.getElementById('status').innerHTML =
		        'Thanks for logging in, ' + response.name + '!';
		      var pages = response.data;
		      console.log("Successfully retireved pages", pages);
				
				var ul = document.getElementById("list");
				for (var i = 0; len = pages.length; i++) {
					var page = pages[i];
					var li = document.createElement('li');
					var a = document.createElement('a');
					a.href = "#";
					a.onclick = subscribeApp.bind(this, page.id,
							page.access_token);
					a.innerHTML = page.name;
					li.appendChild(a);
					ul.appendChild(li);
				}
		    });
		  }
		
	</script>
	<!-- <button onclick="myFacebookLogin()">Login with Facebook</button> -->
	<!--
	  Below we include the Login Button social plugin. This button uses
	  the JavaScript SDK to present a graphical Login button that triggers
	  the FB.login() function when clicked.
	-->

	<fb:login-button scope="manage_pages" onlogin=checkLoginState();">
	</fb:login-button>

	<div id="status">
	</div>
	<ul id="list"></ul>
</body>
</html>