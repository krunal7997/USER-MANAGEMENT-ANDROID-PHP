<?php 
     
 
    require 'connection.php';
    $id=$_POST['id'];
 
    $deleteuser=mysqli_query($con,"DELETE FROM user WHERE id='$id'");
 
    if($deleteuser>0){
 
        $response['error']=200;
        $response['message']="Account has been deleted";
    }
    else{
 
        $response['error']=400;
        $response['message']="Account has not been deleted";
    }
 
    echo json_encode($response);
 ?>