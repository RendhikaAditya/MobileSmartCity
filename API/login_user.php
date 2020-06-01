<?php
    require_once('koneksi.php');
    class emp{}
    $email =$_POST['email_user'];
    $password = $_POST['password_user'];
    
    if((empty($email))||(empty($password))){
	    $response = new emp();
	    $response->response = "pastikan data terisi";
	    $response->code= 2;
	    die(json_encode($response));
	}
	    
	   $query = "SELECT * FROM tb_member WHERE email_member='$email' AND password_member='$password'";
	   
	   $ad = mysqli_query($con,$query);
	   $row = mysqli_fetch_array($ad);
        if(!empty($row)){
            $response = new emp();
            $response->id_user = $row['id_member'];
            $response->response = "selamat Datang ";
            $response->code =1;
            die(json_encode($response));
        }else{
            $response = new emp();
            $response->response = "Email dan kata sandi anda tidak cocok. Silahkan coba lagi.";
            $response->code =0;
        die(json_encode($response));
        }

		mysqli_close($con);
	
?>	