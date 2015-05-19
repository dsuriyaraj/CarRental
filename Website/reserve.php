<?php
//$connection= mysqli_connect("localhost:3306","root","","crs");
$connection= mysqli_connect("dbserver.mss.icics.ubc.ca","team06","t3xtb00k","team06");
$name=($_GET['id']);

/*Date_diff function*/
if(!function_exists('date_diff')) {
  class DateInterval {
    public $y;
    public $m;
    public $d;
    public $h;
    public $i;
    public $s;
    public $invert;
    public $days;
 
    public function format($format) {
      $format = str_replace('%R%y', 
        ($this->invert ? '-' : '+') . $this->y, $format);
      $format = str_replace('%R%m', 
         ($this->invert ? '-' : '+') . $this->m, $format);
      $format = str_replace('%R%d', 
         ($this->invert ? '-' : '+') . $this->d, $format);
      $format = str_replace('%R%h', 
         ($this->invert ? '-' : '+') . $this->h, $format);
      $format = str_replace('%R%i', 
         ($this->invert ? '-' : '+') . $this->i, $format);
      $format = str_replace('%R%s', 
         ($this->invert ? '-' : '+') . $this->s, $format);
 
      $format = str_replace('%y', $this->y, $format);
      $format = str_replace('%m', $this->m, $format);
      $format = str_replace('%d', $this->d, $format);
      $format = str_replace('%h', $this->h, $format);
      $format = str_replace('%i', $this->i, $format);
      $format = str_replace('%s', $this->s, $format);
 
      return $format;
    }
  }
 
  function date_diff(DateTime $date1, DateTime $date2) {
 
    $diff = new DateInterval();
 
    if($date1 > $date2) {
      $tmp = $date1;
      $date1 = $date2;
      $date2 = $tmp;
      $diff->invert = 1;
    } else {
      $diff->invert = 0;
    }
 
    $diff->y = ((int) $date2->format('Y')) - ((int) $date1->format('Y'));
    $diff->m = ((int) $date2->format('n')) - ((int) $date1->format('n'));
    if($diff->m < 0) {
      $diff->y -= 1;
      $diff->m = $diff->m + 12;
    }
    $diff->d = ((int) $date2->format('j')) - ((int) $date1->format('j'));
    if($diff->d < 0) {
      $diff->m -= 1;
      $diff->d = $diff->d + ((int) $date1->format('t'));
    }
    $diff->h = ((int) $date2->format('G')) - ((int) $date1->format('G'));
    if($diff->h < 0) {
      $diff->d -= 1;
      $diff->h = $diff->h + 24;
    }
    $diff->i = ((int) $date2->format('i')) - ((int) $date1->format('i'));
    if($diff->i < 0) {
      $diff->h -= 1;
      $diff->i = $diff->i + 60;
    }
    $diff->s = ((int) $date2->format('s')) - ((int) $date1->format('s'));
    if($diff->s < 0) {
      $diff->i -= 1;
      $diff->s = $diff->s + 60;
    }
 
    $start_ts   = $date1->format('U');
    $end_ts   = $date2->format('U');
    $days     = $end_ts - $start_ts;
    $diff->days  = round($days / 86400);
 
    if (($diff->h > 0 || $diff->i > 0 || $diff->s > 0))
      $diff->days += ((bool) $diff->invert)
        ? 1
        : -1;
 
    return $diff;
 
  }
 
}
/***************************************/

//drop down list
$list_location="";
$list_vtype="";
$list_equipment="";
static $list_vehicle="";

//selected item from list
static $l_location="";
static $l_type="";
static $category=""; //get it from l_type
static $l_add_equip="";
static $branch_id=""; //get it from l_location
$vlicense="";
$pickup_time="";
$c_pickup_time=""; //date time object for date_diff
$dropoff_time="";
$c_dropoff_time="";
$cost="";

//display message
$message="";
$availability="";

//Validation
$validation1="";
$validation2="";
$validation3="";
$validation_final="";

//Location drop down
$query= "select distinct Location from Branch where Location <> COALESCE('{$l_location}', 'null')";
$result=mysqli_query($connection,$query);

while ($row = mysqli_fetch_assoc($result))
{
	$list_location.="<option value='".$row['Location']."'>".$row['Location']."</option>";
}
mysqli_free_result($result);

//Vehicle type drop down
$query= "select distinct Vtype_name from VehicleType";
$result=mysqli_query($connection,$query);

while ($row = mysqli_fetch_assoc($result))
{
	$list_vtype.="<option value='".$row['Vtype_name']."'>".$row['Vtype_name']."</option>";
}
mysqli_free_result($result);

//echo $l_location." ".$l_type." ".$category." ".$pickup_time." ".$dropoff_time."<br/>";

if(isset($_POST["location"]) && $_POST["location"]!="")
{
	$l_location=$_POST["location"];
	$list_location="";
	
	$query= "select distinct Location from Branch where Location <> COALESCE('{$l_location}', 'null')";
	$result=mysqli_query($connection,$query);
	
	while ($row = mysqli_fetch_assoc($result))
	{
		$list_location.="<option value='".$row['Location']."'>".$row['Location']."</option>";
	}
	mysqli_free_result($result);
	
	$query = "select BranchID from Branch where Location='{$l_location}'";
	$result = mysqli_query($connection,$query);
	
	while ($row = mysqli_fetch_assoc($result))
	{
		$branch_id=$row['BranchID'];
	}
	mysqli_free_result($result);
}

if(isset($_POST["type"]) && $_POST["type"]!="")
{
	$l_type=$_POST["type"];
	$list_vtype="";
	
	$query= "select distinct Vtype_name from VehicleType where Vtype_name <> COALESCE('{$l_type}', 'null')";
	$result=mysqli_query($connection,$query);

	while ($row = mysqli_fetch_assoc($result))
	{
		$list_vtype.="<option value='".$row['Vtype_name']."'>".$row['Vtype_name']."</option>";
	}
	mysqli_free_result($result);
	
	//Category (Car or truck)
	if ($l_type=="Cargo Van" || $l_type=='Box Truck' || $l_type=='12-foot' ||$l_type== '15-foot' || $l_type=='24-foot') 
	{
		$category="Truck";
	}
	else 
		if($l_type=="empty")
		{
			$category="null";
		}
		else
		{
			$category="Car";
		}
	
	$l_add_equip="";
	$list_equipment="";
	$query= "select EquipmentName from Additional_equipment where VehicleCategory = '{$category}'";
	$result=mysqli_query($connection,$query);
	
	while ($row = mysqli_fetch_assoc($result))
	{
		$list_equipment.="<option value='".$row['EquipmentName']."'>".$row['EquipmentName']."</option>";
	}
	mysqli_free_result($result);
}

//list vehicles
if(isset($_POST["List_Vehicles"]))
{
	//validation1
	if ($branch_id=="" || $l_type=="" )
	{
		$validation1="(*)Location/Type can't be left NULL";
		
	}	
	else
	{
		$validation1="";
		$query = "Select Vlicense,Vname,Year from Vehicle where BranchID ='{$branch_id}' and Vtype_name='{$l_type}' and Status=1 order by Vlicense";
		$result = mysqli_query($connection,$query);

		$list_vehicle = " <table id='content'><tr><th>License No.</th><th>Name</th><th>Year</th></tr>";

		while ($row = mysqli_fetch_assoc($result)) 
		{

			$list_vehicle .= "<tr>";
			$list_vehicle .= "<td style='width:100px'>".$row['Vlicense']."</td>";
			$list_vehicle .= "<td style='width:150px'>".$row['Vname']."</td>";
			$list_vehicle .= "<td style='width:70px'>".$row['Year']."</td>";
			$list_vehicle .="</tr>";
		}

		$list_vehicle .="</table>";
		mysqli_free_result($result);
		
		
	}
}

if(isset($_POST["vlicense"]))
{
	$vlicense=$_POST["vlicense"];
}

if(isset($_POST["pickup_time"]))
{
	$pickup_time=$_POST["pickup_time"];
	$c_pickup_time = new DateTime($pickup_time);
	
}

if(isset($_POST["dropoff_time"]))
{
	$dropoff_time=$_POST["dropoff_time"];
	$c_dropoff_time = new DateTime($dropoff_time);
}

//echo $l_location." ".$l_type." ".$category." ".$pickup_time." ".$dropoff_time."<br/>";

//Vehicle Availability functionality (button pressed)
if(isset($_POST["Available"]))
{
	//validation2
	if ($branch_id=="" || $l_type=="" || $vlicense=="")
	{
		if ($branch_id=="" || $l_type=="" )
		{
			$validation2.="(*)Location/Type can't be left NULL<br/>";
		}
		if ($vlicense=="" )
		{
			$validation2.="(*)Enter the Vehicle License# from the list";
		}
	}
	else
	{
		$num_rows=0;		
		$query= "select * from Vehicle where BranchID='{$branch_id}' and Vlicense='{$vlicense}' and Vtype_name='{$l_type}'";
		$result=mysqli_query($connection,$query);
		$num_rows = mysqli_num_rows($result);
		
		if($num_rows==0 || $pickup_time=="" || $dropoff_time=="" || ($pickup_time>$dropoff_time))
		{
			if($num_rows==0)
			{
				$validation2.="(*) Enter the Valid Vehicle License# listed <br/>";
			}
			
			if($pickup_time=="" || $dropoff_time=="" || ($pickup_time>$dropoff_time))
			{
				$validation2.="(*) Enter Valid pick up and drop off time";
			}
			mysqli_free_result($result);
		}
	}	
	if($validation2=="")
	{		
		$query=	"select Vlicense from Vehicle v where 1=1 and v.Vlicense not in
				(select Vlicense from ((select distinct v1.Vlicense from RentalAgreement r,Vehicle v1 where 1=1
				and v1.Vlicense=r.Vlicense 
				and r.Dropoff_time >'{$pickup_time}'  and r.Pickup_time < '{$dropoff_time}')
				union
				(select distinct v2.Vlicense from Reservation res, Vehicle v2
				where 1=1
				and v2.Vlicense=res.Vlicense 
				and res.Dropoff_time > '{$pickup_time}'
				and res.Pickup_time < '{$dropoff_time}')) t1)
				and v.Vlicense={$vlicense}";
				
				
		$result=mysqli_query($connection,$query);
		if(!$result)
		{
		  $message="Database Query Failed".mysqli_error($connection);;
		}
		
		if(mysqli_fetch_assoc($result))
		{
		  $availability="&nbsp;<font color=\"green\">Available</font>";
		}
		else
		{
		  $availability="&nbsp;<font color=\"red\">Not Available</font>";
		}
		mysqli_free_result($result);
	}
			
}

if(isset($_POST["Additional_equipment"]) && $_POST["Additional_equipment"]!="")
{
	$list_equipment="";
	$l_add_equip=$_POST["Additional_equipment"];
	$query= "select EquipmentName from Additional_equipment where VehicleCategory = '{$category}' and EquipmentName <> COALESCE('{$l_add_equip}', 'null')";
	$result=mysqli_query($connection,$query);
	
	while ($row = mysqli_fetch_assoc($result))
	{
	$list_equipment.="<option value='".$row['EquipmentName']."'>".$row['EquipmentName']."</option>";
		
	}
	
	mysqli_free_result($result);
}

	
//Cost Estimation functionality (button pressed)
if(isset($_POST["cost"]))
{	
	//Validation3
	if($pickup_time=="" || $dropoff_time=="" || ($pickup_time>$dropoff_time))
	{
		if($pickup_time=="" || $dropoff_time=="" || ($pickup_time>$dropoff_time))
		{
			$validation3.="(*) Enter Valid pick up and drop off time";
		}
	}
	
	//Category (Car or truck)
	if ($l_type=="Cargo Van" || $l_type=='Box Truck' || $l_type=='12-foot' ||$l_type== '15-foot' || $l_type=='24-foot') 
	{
		$category="Truck";
	}
	else 
		if($l_type=="empty")
		{
			$category="";
		}
		else
		{
			$category="Car";
		}
	
	$num_rows=0;		
	$query= "select * from Additional_equipment where EquipmentName like'%{$l_add_equip}%' and VehicleCategory like '%{$category}%'";
	$result=mysqli_query($connection,$query);
	$num_rows = mysqli_num_rows($result);
    
	if($num_rows==0)
	{
		$validation3.="(*) Enter the Valid Additional Equipment";
	}
	
	if($validation3=="")
	{
		//Initialization
		$minutes=0;
		$hours=0;
		$days=0;
		$weeks=0;
		$v_weekly_rate=0;
		$v_daily_rate=0;
		$v_hourly_rate=0;
		$Ins_wrate=0;
		$Ins_drate=0;
		$Ins_hrate=0;
		$e_daily_rate=0;
		$e_hourly_rate=0;

		//no of days calculation
		$duration = date_diff($c_pickup_time, $c_dropoff_time);

		$minutes=$duration->format('%i');
		$hours=$duration->format('%h');
		$days=$duration->format('%d');

		if ($hours>10)
		{
			$days=$days+1;
			$hours=0;
		}

		$e_days=$days; //for calculating equipment cost, based on no of days
		$weeks=intval($days/7);
		$days=$days%7;
		
		//echo "Weeks: ".$weeks." days".$days." hours: ".$hours." Minutes: ".$minutes;

		//Rates for the selected vehicle type
		$query="select Weekly_rate,Daily_rate,Hourly_rate,Ins_wrate,Ins_drate,Ins_hrate from VehicleType where Vtype_name='{$l_type}'";
		$result=mysqli_query($connection,$query);
		
		while ($row = mysqli_fetch_assoc($result)) 
		{
			 $v_weekly_rate=$row['Weekly_rate'];
			 $v_daily_rate=$row['Daily_rate'];
			 $v_hourly_rate=$row['Hourly_rate'];
			 $Ins_wrate=$row['Ins_wrate'];
			 $Ins_drate=$row['Ins_drate'];
			 $Ins_hrate=$row['Ins_hrate'];
		}
		mysqli_free_result($result);

		//Additional Equipment rates (if selected any)
		$query="select Daily_rate,Hourly_rate from Additional_equipment where EquipmentName='{$l_add_equip}'";
		$result=mysqli_query($connection,$query);
		
		while ($row = mysqli_fetch_assoc($result)) 
		{
			 $e_daily_rate=$row['Daily_rate'];
			 $e_hourly_rate=$row['Hourly_rate'];
		}
		mysqli_free_result($result);

		//Cost Estimation
		$cost=$weeks*($Ins_wrate+$v_weekly_rate)+
			  $days*($Ins_drate+$v_daily_rate)+$e_days*($e_daily_rate)+
			  $hours*($Ins_hrate+$e_hourly_rate+$v_hourly_rate)+
			  ($minutes/60)*($Ins_hrate+$e_hourly_rate+$v_hourly_rate);
		$cost=number_format(round($cost,2),2);
		
		$cost.=" CAD";
	}
}      

//Reservation
if(isset($_POST["Reserve"]))
{
	//validation1
	if ($branch_id=="" || $l_type=="" )
	{
		$validation_final.="(*)Location/Type can't be left NULL<br/>";
	}	
	else
	{
		if($vlicense!="")
		{
			//validation2
			$num_rows=0;		
			$query= "select * from Vehicle where BranchID='{$branch_id}' and Vlicense={$vlicense} and Vtype_name='{$l_type}'";
			$result=mysqli_query($connection,$query);
			$num_rows = mysqli_num_rows($result);
			
			if($num_rows==0 || $pickup_time=="" || $dropoff_time=="" || ($pickup_time>$dropoff_time))
			{
				if($num_rows==0)
				{
					$validation_final.="(*) Enter the Valid Vehicle License# listed <br/>";
				}
				
				if($pickup_time=="" || $dropoff_time=="" || ($pickup_time>$dropoff_time))
				{
					$validation_final.="(*) Enter Valid pick up and drop off time<br/>";
				}
				mysqli_free_result($result);
			}
			else
			{		
				$num_rows=0;
				mysqli_free_result($result);
				$query=	"select Vlicense from Vehicle v where 1=1 and v.Vlicense not in
						(select Vlicense from ((select distinct v1.Vlicense from RentalAgreement r,Vehicle v1 where 1=1
						and v1.Vlicense=r.Vlicense 
						and r.Dropoff_time >'{$pickup_time}'  and r.Pickup_time < '{$dropoff_time}')
						union
						(select distinct v2.Vlicense from Reservation res, Vehicle v2
						where 1=1
						and v2.Vlicense=res.Vlicense 
						and res.Dropoff_time > '{$pickup_time}'
						and res.Pickup_time < '{$dropoff_time}')) t1)
						and v.Vlicense={$vlicense}";
								
				$result=mysqli_query($connection,$query);
				$num_rows = mysqli_num_rows($result);
				
				if($num_rows==0)
				{
					$validation_final.="(*) Entered Vehicle is currently not available for this rental period <br/>";
				}
				mysqli_free_result($result);
			}	
			
		}
		else
		{
			$validation_final.="(*) Vehicle License# can't be left NULL<br/>";
		}
	}		
	
	//Validation3
	//Category (Car or truck)
	if ($l_type=="Cargo Van" || $l_type=='Box Truck' || $l_type=='12-foot' ||$l_type== '15-foot' || $l_type=='24-foot') 
	{
		$category="Truck";
	}
	else 
		if($l_type=="empty")
		{
			$category="";
		}
		else
		{
			$category="Car";
		}
	
	$num_rows=0;		
	$query= "select * from Additional_equipment where EquipmentName like'%{$l_add_equip}%' and VehicleCategory like '%{$category}%'";
	$result=mysqli_query($connection,$query);
	$num_rows = mysqli_num_rows($result);
    
	if($num_rows==0)
	{
		$validation_final.="(*) Enter the Valid Additional Equipment";
	}
	
	if($validation_final=="")
	{
		//Phone_number
		$phone_number="";
		$query= "select Phone_number from Online_Customer where Username='{$name}'";
		$result=mysqli_query($connection,$query);
		
		while ($row = mysqli_fetch_assoc($result))
		{
			$phone_number=$row['Phone_number'];
		}
		mysqli_free_result($result);
		
		$l_add_equip1=$l_add_equip; //to eliminate the conflict on the add_quip drop down list
		
		if($l_add_equip1=="")
		{
			$l_add_equip1="null";
		}
		else
		{
			$l_add_equip1="'".$l_add_equip1."'";
		}
		
		$query= "INSERT INTO Reservation (Phone_number, Vtype_name, Vlicense, BranchID, Pickup_time, Dropoff_time, Equipment) "; 
		$query.="VALUES ('{$phone_number}', '{$l_type}', {$vlicense}, {$branch_id}, '{$pickup_time}', '{$dropoff_time}', {$l_add_equip1}) ";
		//echo $query;
		$result=mysqli_query($connection,$query);
		
		if($result && mysqli_affected_rows($connection)==1)
		{
			$confno="";
			$query= "select max(Confno) AS 'Confno' from Reservation where Phone_number='{$phone_number}'";
			$result1=mysqli_query($connection,$query);
			while ($row = mysqli_fetch_assoc($result1)) 
			{
				 $confno=$row['Confno'];
			}
			
			$message="Reservation successful and the Reservation ID is ".$confno;
			mysqli_free_result($result1);
		}

		else
		{	
			$message="Database Query Failed: Reservation".mysqli_error($connection);
		}
	}
}
mysqli_close($connection);

?>

<!DOCTYPE html>
	
<html>

<head>
	<title>
	  Reserve
	</title>
    <link rel="stylesheet" href="themes/default/default.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="themes/pascal/pascal.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="themes/orman/orman.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="nivo-slider.css" type="text/css" media="screen" />
    <link rel="stylesheet" type="text/css" href="css/common.css" />
	<link rel="stylesheet" type="text/css" href="css/style.css" />
	 <script type="text/javascript" src="scripts/jquery-1.7.1.min.js"></script>
	 <script language="javascript" type="text/javascript" src="scripts/_javascript.js"></script>
    <script type="text/javascript" src="jquery.nivo.slider.pack.js"></script>
	
	<meta charset="UTF-8" />
	
<script language="javascript" type="text/javascript" src="date.js"></script>

<style>th {background-color: black; color: white}</style>

	

</head>

<body>
<div id="header1">
<div id="LikeUs">
Follow us: 
			<a href="https://www.facebook.com/superrentcarrental"><img src="images/facebook.jpeg" alt="FaceBook" height="20" width="49" /></a> 
			<a href="https://twitter.com/"><img src="images/Twitter.jpg" alt="FaceBook" height="20" width="49" /></a>
			
			</div>
            
        													
															<div id="header"> 
															<h1>SuperRent Car Rental </h1>
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
<div id="wrapper" style="height: 1370px">
       
		
											


<!--  Flash Code Start-->
<div id="Flash">
<!--  Flash Code Start-->
<div class="slider-wrapper theme-default">
            <div class="ribbon"></div>
            <div id="slider" class="nivoSlider">
                <img src="images/Car6.jpg"  alt="" title="Top Rated Car Rental Service in City"  />
                <img src="images/Car7.jpg" alt=""  title="Economy to Luxury type of Car" />
                <img src="images/Car8.jpg"  alt="" title="Branches located all across the City" data-transition="slideInLeft" />

            </div>
            
        </div>

    
    
   
    <script type="text/javascript">
    $(window).load(function() {
        $('#slider').nivoSlider();
    });
    </script>
<!--Flash Code End-->                     
</div>

<div class="clear"> </div>
 <br/>		
                   											
		
 <div id="reg">
<form id="reserve" method="post" action="reserve.php?id=<?php echo $name; ?>" class="auto-style3">
<h3 class="auto-style12">Reservation</h3>

<table>
		<tr>
			<td>
					<table style=" height: 150px">
					  <tr>
					  <td valign="top" class="auto-style11" style="width: 150px; height: 95px;">Select location<sup>*</sup>:</td>
					  <td valign="top" style="width: 200px; height: 50px;">
						<select name="location" id="Loc" style="width: 200px; height: 25px">
							<option value="<?php echo $l_location ;?>"><?php echo $l_location;?></option>
							<?php echo $list_location;?>
						</select></td>
					  </tr>
					  <tr>
					  <td valign="top" class="auto-style11" style="width: 150px">Select type<sup>*</sup>:</td>
					  <td valign="top" style="width: 200px">
					    <select name="type" style="width: 200px;height: 25px">
							<option value="<?php echo $l_type ;?>"><?php echo $l_type;?></option>
							<?php echo $list_vtype;?>
						</select></td>
					  </tr>

					  <tr>
					  	  <td>
					  	  		<div style="width: 200px; height: 50px;"> </div>
					  	  </td>
						  <td>  </br>
								<input name="List_Vehicles" style="float: right; width: 125px; height: 30px;" type="submit" value="List vehicles">
						  </td>
					  </tr>
				</table>
			</td>
				  
			
		<td id="table" valign="top">
			  <?php echo $list_vehicle;?> 
			  <br/>&nbsp;&nbsp;<font color="red"><p><?php echo $validation1; ?></p></font>
  			
		</td>
		
		

		</tr>
		
</table>

  <br/><br/><br/>
  

  <table style="width: 625px; height: 180px">
	<tr>
	  <td class="auto-style11" style="width: 194px">Enter Vehicle License<sup>*</sup>:</td>
	  <td style="width: 330px">
		<input style="width: 190px;height: 20px;" type="Text" id="Vlicense" name="vlicense" value="<?php echo $vlicense;?>" maxlength="3" size="35"> 
	  </td>
    </tr>

	<tr>
	  <td class="auto-style11" style="width: 194px">Pick up date and time<sup>*</sup>:</td>
	  <td style="width: 330px">
		<input style="width: 190px;height: 20px;" type="Text" id="date1" name="pickup_time" value="<?php echo $pickup_time;?>" maxlength="25" size="25" class="auto-style11"><a href="javascript:NewCssCal ('date1','yyyyMMdd','dropdown',true,'24',true)"><img src="images2/cal.jpg" width="16" height="16" border="0" alt="Pick a date" class="auto-style11"></a><span class="auto-style11">
		</span>
	  </td>
	</tr>
	  
	<tr>
	  <td class="auto-style11" style="width: 194px">Drop off date and time<sup>*</sup>:</td>
	  <td style="width: 330px">
		<input style="width: 190px;height: 20px;" type="Text" id="date2" name="dropoff_time" value="<?php echo $dropoff_time;?>" maxlength="25" size="25" class="auto-style11"><a href="javascript:NewCssCal ('date2','yyyyMMdd','dropdown',true,'24',true)"><img src="images2/cal.jpg" width="16" height="16" border="0" alt="Pick a date" class="auto-style11"></a><span class="auto-style11">
		</span>
      </td>
	</tr>
  

  
  </table>

	<div class="style22" style="width: 700px; height: 60px;">
	<input name="Available" style="width: 125px; height: 30px;" type="submit" value="Check Availability">
	<?php echo $availability; ?></font>
	&nbsp;&nbsp;<font color="red"> <?php echo "<br/><br/>".$validation2; ?></font>
	
	</div>
	
	
	
<table style="width: 670px; height: 153px">

<tr>
  <td class="auto-style16" style="width: 175px">Additional Equipment:</td>
  <td style="width: 330px" class="style15">

  
	<select name="Additional_equipment"  style="width: 190px;height: 25px">
		<option value="<?php echo $l_add_equip ;?>"><?php echo $l_add_equip;?></option>
		<option value=""></option>
	
		<?php echo $list_equipment;?>
	
	</select><span class="auto-style11"> </span>
	

  </td>
  </tr>

 </table>
 
 <div class="style22" style="width: 700px; height: 60px;">
	<input name="cost" style="width: 125px; height: 30px;" type="submit" value="Estimate Cost">
	<font color="Green"><b><?php echo $cost;?></b></font>
	<br/><br/><font color="red"> <?php echo $validation3; ?></font></div>
	
	<div class="style22" style="width: 631px; height: 350px;">
	<p class="style15"> (* : Required field)</p>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<input name="Reserve" style="width: 150px; height: 40px;" type="submit" value="Reserve" >
			<br/><br/><font color="green"><?php echo $message;?></font>
			<font color="red"> <?php echo $validation_final; ?></font>
			<br/><br/>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a style="width: 150px; height: 10px; background-color: #999; padding: 10px; color:white;" href="index.php">Logout</a>

			</div>

	</form>


</div>			 
 

 
</div>

<br/>




<!------------------------------------Do not edit below this------------------------------------------>	

	

<div class="footer">


<div id="right">
Designed by: <span class="auto-style9"> <strong>MSS 2015 Team#5</strong></span></div>

</div>
			

</script>
</body>
</html>
