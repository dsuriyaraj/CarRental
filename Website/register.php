<?php
  function redirect_to($location)
  {
    header("Location: ".$location);
	exit;
  }
  
  //$connection= mysqli_connect("localhost","root","","crs");
  $connection= mysqli_connect("dbserver.mss.icics.ubc.ca","team06","t3xtb00k","team06");
  
  //Form Variables
  $first_name="";
  $last_name="";
  $name="";
  $address="";
  $city="";
  $phone="";
  $roadstar="";
  $username="";
  
  //message display variables
  $message="";
  $available="";
  $errors = array();
  $output="";
  
  //Checking validation once the submit button is clicked
  if(isset($_POST["Submit1"]))
  {
	$first_name=$_POST["fname"];
	$last_name=$_POST["lname"];
	$name=(trim($_POST["fname"]))." ".(trim($_POST["lname"]));
	$address=(trim($_POST["faddress"]));
	$city=trim($_POST["fcity"]);
	$phone=$_POST["phone1"]."-".$_POST["phone2"]."-".$_POST["phone3"];
	$username=strtolower(trim($_POST["uname"]));
	$password=trim($_POST["pword"]);
	$confirm_password=trim($_POST["cpword"]);
	$roadstar=0;
	
	//real escape string(to escape from ', and from sql injection)
	$name=mysqli_real_escape_string($connection,$name);
	$address=mysqli_real_escape_string($connection,$address);
	$city=mysqli_real_escape_string($connection,$city);
	$username=mysqli_real_escape_string($connection,$username);
	
	//Validations
	//1.Not null
	
	if (!isset($name) || $name === " ") 
	{
		$errors['Name'] = "Value can't be left blank";
	}
	if (!isset($address) || $address === "") 
	{
		$errors['Address'] = "Value can't be left blank";
	}
	if (!isset($city) || $city === "") 
	{
		$errors['City'] = "Value can't be left blank";
	}
	if (!isset($phone) || $phone === "") 
	{
		$errors['Phone'] = "Value can't be left blank";
	}
	if (!isset($roadstar) || $roadstar === "") 
	{
		$errors['Roadstar'] = "Value can't be left blank";
	}
	if (!isset($username) || $username === "") 
	{
		$errors['Username'] = "Value can't be left blank";
	}
	if (!isset($password) || $password === "") 
	{
		$errors['Password'] = "Value can't be left blank";
	}
	if (!isset($confirm_password) || $confirm_password === "") 
	{
		$errors['Confirm_password'] = "Value can't be left blank";
	}
	
	//phone number format
	if(!is_numeric($_POST["phone1"]) || !is_numeric($_POST["phone2"]) || !is_numeric($_POST["phone3"]))
	{
		$errors['Phone No'] = "Invalid number format, it should contain only numbers";
	}
	
	if(strlen($_POST["phone1"])!=3 || strlen($_POST["phone2"])!=3 ||strlen($_POST["phone3"])!=4)
	{
		$errors['Phone number'] = "Invalid number format, it should be (xxx-xxx-xxxx)";
	}
	
	//Password match
	if($password!=$confirm_password)
	{
	  $errors['Passwords']="didn't match";
	}
	
	//2.Uniqueness
	//username
	$query= "select Username from Online_Customer where lower(Username)= '{$username}' ";
	$result=mysqli_query($connection,$query);
	
	if(mysqli_fetch_assoc($result))
	{
	  $errors['Username'] = "Already exists, choose different user name";
	}
    mysqli_free_result($result);
	
	//phone number
	$query= "select Phone_number from Online_Customer where Phone_number= '{$phone}' ";
	$result=mysqli_query($connection,$query);
	
	if(mysqli_fetch_assoc($result))
	{
	  $errors['Phone_number'] = "Online Account already exists for this phone number, contact branch if you forgot the username/password";
	}
    mysqli_free_result($result);
	
	//display the validation errors
	function form_errors($errors=array()) 
	{
	  $output = "";
	  
	  if (!empty($errors)) 
	  {
		foreach ($errors as $key => $error) 
		{
			$output .= "(*){$key}: {$error}<br/>";
		}
	  }
	  
	  return $output;
	}
	
	$output=form_errors($errors);
	
	//Roadstar
	if($_POST["rdstar"]=="yes")
	{
		$roadstar=1;
	}	
	
	//perform database query
	if($output=="")
	{
		$query= "select Phone_number from Customer where Phone_number= '{$phone}' ";
		$result=mysqli_query($connection,$query);
		
		if(!mysqli_fetch_assoc($result))
		{
			$query= "INSERT INTO Customer ( Phone_number ,  Name ,  Address , City, Clubmember, Roadstar ) ";
			$query.="VALUES ('{$phone}','{$name}','{$address}','{$city}',0, {$roadstar}) ";
			$result=mysqli_query($connection,$query);
			
			if($result && mysqli_affected_rows($connection)==1)
			{
				$query= "INSERT INTO Online_Customer ( Phone_number, Username, Password) ";
				$query.="VALUES ('{$phone}','{$username}','{$password}') ";
				$result=mysqli_query($connection,$query);
				
				if($result && mysqli_affected_rows($connection)==1)
				{
					$message="Registration successful, kindly login with the registered credentials to reserve the car";
				}
				
				else
				{	
					$message="Database Query Failed: reg customer".mysqli_error($connection);
				}
			}
			
			else
			{	
				$message="Database Query Failed: Customer".mysqli_error($connection);
			}
		}	
		
		else
		{
			$query= "INSERT INTO Online_Customer ( Phone_number, Username, Password) ";
			$query.="VALUES ('{$phone}','{$username}','{$password}') ";
			$result=mysqli_query($connection,$query);
			
			if($result && mysqli_affected_rows($connection)==1)
			{
				$message="Registration successful, kindly proceed with login to reserve the car";
			}
			
			else
			{	
				$message="Database Query Failed: reg customer".mysqli_error($connection);
			}
		}
    }	
  }
  mysqli_close($connection);
?>

<!DOCTYPE html>
	
<html>

<head>
	<title>
	  Register
	</title>
    <link rel="stylesheet" href="themes/default/default.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="themes/pascal/pascal.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="themes/orman/orman.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="nivo-slider.css" type="text/css" media="screen" />
    <link rel="stylesheet" type="text/css" href="css/common.css" />
	<link rel="stylesheet" type="text/css" href="css/style.css" />
	<meta charset="UTF-8" />
	
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
																											<li><a href="aboutus.php">About us </a></li>
																											<li><a href="login.php">Reserve </a></li>
                                                                                                            <li><a href="contact.php">Contact us</a></li>

																											
																										</ul>
																						</div>
													<div class="clear"> </div>									
											</div>	


<!--------------------------------------------------------------------------------- Start of Common --------------------------------------------------->
<div id="wrapper" style="height: 1120px">
       
		
											


<!--  Flash Code Start-->
<div id="Flash">
<!--  Flash Code Start-->
<div class="slider-wrapper theme-default">
            <div class="ribbon"></div>
            <div id="slider" class="nivoSlider">
                 <img src="images/Car.jpg"  alt="" title="Top Rated Car Rental Service in City"  />
                <img src="images/Car1.jpg"  alt="" title="Economy to Luxury type Car" />
                <img src="images/Car2.jpg"  alt="" title="Branches located all across the City" data-transition="slideInLeft" />
                
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

<div class="clear"> </div>
 <br/>		
<!--Flash Code End-->                     


											
		
<!--------------------------------------------------------------------------------- End of Common --------------------------------------------------->	

<div id="reg">
<form method="post" action="register.php" class="auto-style3">
<table style="width: 641px; height: 480px">
  <tr>
  <td colspan=2 style="height: 48px">
    <h3 class="auto-style12">REGISTRATION</h3>
  </td>
  </tr>
  <tr>
  <td class="auto-style11" style="width: 176px"><span class="auto-style11">First Name</span><sup><span class="auto-style11">*</span></sup><span class="auto-style11">:</span></td>
  <td style="width: 330px">
    <input type="text" name="fname" value="<?php echo $first_name;?>" style="width: 250px" class="auto-style11" /><span class="auto-style11">
	</span>
  </td>
  </tr>
  <tr>
  <td class="auto-style11" style="width: 176px">Last Name<sup>*</sup>:</td>
  <td style="width: 330px">
    <input type="text" name="lname" value="<?php echo $last_name;?>"style="width: 250px" class="auto-style11" /><span class="auto-style11">
	</span>
  </td>
  </tr>

  <tr>
  <td class="auto-style11" style="width: 176px">Address<sup>*</sup>:</td>
  <td style="width: 330px">
    <input type="text" name="faddress" value="<?php echo $address;?>" style="width: 250px" class="auto-style11" /><span class="auto-style11">
	</span>
  </td>
  </tr>

 <tr>
  <td class="auto-style11" style="width: 176px">City<sup>*</sup>:</td>
  <td style="width: 330px">
    <input type="text" name="fcity" value="<?php echo $city;?>" style="width: 250px" class="auto-style11" /><span class="auto-style11">
	</span>
  </td>
  </tr>
<tr>
  <td class="auto-style11" style="width: 176px">Phone<sup>*</sup>:</td>
  <td style="width: 330px">
    <input type="tel" name="phone1"  value="<?php echo substr(preg_replace('/\D/', '', $phone),0,3)?>" maxlength="3" style="width: 50px" class="auto-style11" />
    <input type="tel" name="phone2"  value="<?php echo substr(preg_replace('/\D/', '', $phone),3,3)?>" maxlength="3" style="width: 50px" class="auto-style11" />
    <input type="tel" name="phone3"  value="<?php echo substr(preg_replace('/\D/', '', $phone),6,4)?>" maxlength="4" style="width: 65px" class="auto-style11" /><span class="auto-style11">
	</span>

  </td>
  </tr>
   <tr>
  <td class="auto-style11" style="width: 176px">Roadstar<sup>*</sup>:</td>
  <td style="width: 330px">
  <input type="radio" name="rdstar" value="yes" class="auto-style11"><span class="auto-style11"> Yes
  </span>
  <input type="radio" name="rdstar" value="no" checked="checked" class="auto-style11"><span class="auto-style11"> No
  </span>
  </td>
  </tr>

 <tr>
  <td class="auto-style11" style="width: 176px">Username<sup>*</sup>:</td>
  <td style="width: 330px">
    <input type="text" name="uname" value="<?php echo $username;?>" style="width: 250px" class="auto-style11" /><span class="auto-style11">
	</span>
  </td>
  </tr>
  <tr>
  <td class="auto-style11" style="width: 176px">Password<sup>*</sup>:</td>
  <td style="width: 330px">
    <input type="password" name="pword" style="width: 250px" class="auto-style11" /><span class="auto-style11">
	</span>
  </td>
  </tr>
  <tr>
  <td class="auto-style11" style="width: 176px">Confirm Password<sup>*</sup>:</td>
  <td style="width: 330px">
    <input type="password" name="cpword" style="width: 250px" class="auto-style11" /><span class="auto-style11">
	</span>
  </td>
  </tr>

</table>

	<div class="style22" style="width: 631px; height: 35px;">
	<p class="style15"> (* : Required field)</p>
	&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input name="Submit1" style="width: 130px; height: 30px;" type="submit" value="Register"></div>
			
	<br/><br/><font color="green"><?php echo $message;?></font>
	<br/><font color="red"> <?php echo $output; ?></font>
					
	</form>
				
</div>			 
 

 
</div>





<!------------------------------------Do not edit below this------------------------------------------>	

	

<div class="footer">


<div id="right">
Designed by: <span class="auto-style9"> <strong>MSS 2015 Team#5</strong></span>
</div>

</div>
			

</body>
</html>
