<?php

if(isset($_POST["List_Vehicles"]))
{
	//validation1
	if ($branch_id=="" || $l_type=="" )
	{
		$validation1="(*)Location/Type can't be left NULL";
		echo $validation1;
	}	
	else
	{
		//$validation1="";
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
		echo $list_vehicle;
		
	}
}
/*
if( isset($_POST['Location']) && isset($_POST['Type'])  )
{	
	$connection = mysqli_connect('localhost', 'root', '','crs');

	$sql1 = "select BranchID from branch where location='".$_POST['Location']."'";
	$result1 = mysqli_query($connection,$sql1);
	$out_branch = mysqli_fetch_assoc($result1);
	$Branch_ID = $out_branch["BranchID"];


	$sql2 = "Select * from vehicle where BranchID=".$Branch_ID." and Vtype_name='".$_POST['Type']."' and Status=1 order by Vlicense";
	$result2 = mysqli_query($connection,$sql2);

	$output = " <table id='content'><tr><th>License No.</th><th>Name</th><th>Year</th></tr>";

	while ($row = mysqli_fetch_array($result2)) {

		$output .= "<tr>";
		$output .= "<td>".$row["Vlicense"]."</td>";
		$output .= "<td>".$row["Vname"]."</td>";
		$output .= "<td>".$row["Year"]."</td>";
		$output .="</tr>";
	}

	$output .="</table>";

	echo $output;
}

if( isset($_POST['Date1']) && isset($_POST['Date2'])  && isset($_POST['Vlicense']))
{	
	$Date1 = $_POST['Date1'];
	$Date2=  $_POST['Date2'];
    
	//Todo
	// Check if there is no conflict given the Vlicense and the Dates
	$output; // Yes or no based on checking in the tables reeservation and rental agreement
	echo $output;
}
*/
?>





