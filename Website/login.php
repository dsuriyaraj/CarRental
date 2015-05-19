<?php
  $connection= mysqli_connect("dbserver.mss.icics.ubc.ca","team06","t3xtb00k","team06");
  
  function redirect_to($location)
  {
    header("Location: ".$location);
	exit;
  }
  
  $username="";
  $message="";
  if(isset($_POST["Submit1"]))
  {
	$username=strtolower(trim($_POST["fMemberNumber"]));
	$password=trim($_POST["fPassword"]);
	
	$query= "select Username,Password from Online_Customer where lower(Username)= '{$username}' and Password='{$password}' ";
	$result=mysqli_query($connection,$query);
	if(!$result)
    {
      $message="Database Query Failed";
    }
	
    if(mysqli_fetch_assoc($result))
	{
	  mysqli_close($connection);
	  redirect_to("reserve.php?id={$username}");
	}
    else
    {
      $message="*Username/Password do not match";
    }
	mysqli_free_result($result);
  }	
  mysqli_close($connection);
?>

<!DOCTYPE html>
	
<html>


<head>
	<title>
	  Login 
	</title>
	
    <link rel="stylesheet" href="themes/default/default.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="themes/pascal/pascal.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="themes/orman/orman.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="nivo-slider.css" type="text/css" media="screen" />
    <link rel="stylesheet" type="text/css" href="css/common.css" />
	<link rel="stylesheet" type="text/css" href="css/csshome.css" />
	<link href="css/style.css" rel="stylesheet" type="text/css" />
    
    
	</head>

<body>




<div id="header1">

<div id="LikeUs">
Follow us: 
			<a href="https://www.facebook.com/superrentcarrental"><img src="images/facebook.jpeg" alt="FaceBook" height="20" width="49" /></a> 
			<a href="https://twitter.com/"><img src="images/Twitter.jpg" alt="FaceBook" height="20" width="49" /></a>
			
			</div>
            

            
															<div id="header"> 
															<h1>SuperRent Car Rental</h1>
															<h2>Car Rental Company since 2012</h2>
															</div>
																
																						<div id="navMenu">
																									<ul id="menu">
																											<li><a href="index.php">Home</a></li>
																											<li><a href="index.php">About us </a></li>
																											<li><a href="login.php">Reserve </a></li>
                                                                                                            <li><a href="contact.php">Contact us</a></li>
																											
																										</ul>
																						</div>
													<div class="clear"> </div>									
											</div>	
















<!--------------------------------------------------------------------------------- Start of Common --------------------------------------------------->
<div id="wrapper" style="height: 383px">
       
		



<div id="Flash1" class="auto-style12">
<!--  Flash Code Start-->
 <div class="slider-wrapper theme-default">
            <div class="ribbon"></div>
            <div id="slider" class="nivoSlider">
                <img src="images/Car3.jpg"  alt="" title="Top Rated Car Rental Service in City"  />
                <img src="images/Car4.jpg" alt=""  title="Economy to Luxury type of Car" />
                <img src="images/Car5.jpg"  alt="" title="Branches located all across the City" data-transition="slideInLeft" />

                
            </div>
            
        </div>

    
    
    <script type="text/javascript" src="scripts/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="jquery.nivo.slider.pack.js"></script>
    <script type="text/javascript">
    $(window).load(function() {
        $('#slider').nivoSlider();
    });
    </script>
<!--Flash Code End-->                     
</div>
 
	
	
<form method="post" action="login.php" style="width: 295px; height: 177px;">
<table style="width: 300px; height: 180px">
  <tr>
  <td colspan=2>
    <h3><em>Login to Reserve </em></h3>
  </td>
  </tr>
  <tr>
  <td class="auto-style10"><strong>Username:</strong></td>
  <td>
    <input type="text" name="fMemberNumber" value="" style="width: 150px" />
  </td>
  </tr>
  <tr>
  <td class="auto-style10"><strong>Password:</strong></td>
  <td>
    <input type="password" name="fPassword" style="width: 150px" />
  </td>
  </tr>
  </table>
 
	<div class="style22" style="width: 300px; height: 87px;">
			
			<input name="Submit1" style="width: 150px; height: 30px;" type="submit" value="Login" class="auto-style15"></div>
	</form>
	
	
<font color="red"><?php echo "<br/><br/>$message<br/>";?></font> 
<p class="auto-style13" style="width: 295px"><strong><em>
<a href="forgetpaswrd.php"><span class="auto-style14">forgot user name or password?</span></a></em></strong></p>

<p class="auto-style4" style="width: 295px"><strong><em>
<a href="register.php">Not a Member? Register</a></em></strong></p>

			
			


	<div>
	</div>


			
			
			


</div>


<div class="clear"> </div>
<br/>

<br/><br/>

<div class="footer">

<div id="right">
Designed by: <span class="auto-style9"> <strong>MSS 2015 Team#5</strong></span>
</div>

</div>



</body>
</html>
