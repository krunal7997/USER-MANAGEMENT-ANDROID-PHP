<?php

require 'connection.php';
$username = $_POST['username'];
$email = $_POST['email'];
$password= md5($_POST['password']);



$checkuser= "SELECT * From user WHERE email='$email'";
$checkQuery = mysqli_query($con,$checkuser);

if (mysqli_num_rows($checkQuery)>0) 
{
	$response['error'] = "002";
	$response['message'] = "User Already Exists";
} 
else 
{
	$insertQuery = "INSERT INTO user (username,email,password) VALUES ('$username','$email','$password')";

$result = mysqli_query($con,$insertQuery);

if ($result) 
{
	$response['error'] = "000";
	$response['message'] = "Registration Successfully Completed";
}
else
{
		$response['error'] = "001";
	$response['message'] = "Registration Failed...";
}

}

echo json_encode($response);


?>