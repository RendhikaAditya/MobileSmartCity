<?php 
    require_once('koneksi.php');
    class emp{}
	$nama = $_POST['nama_user'];
    $email =$_POST['email_user'];
    $password = $_POST['password_user'];
    $tgl = date('d M Y');
    $veri = "0";
    $num_rows = mysqli_num_rows(mysqli_query($con, "SELECT * FROM tb_member WHERE email_member='$email'"));
    if ($num_rows == 0){
        $query = mysqli_query($con, "INSERT INTO tb_member (nama_member, email_member, password_member,foto_member, thumb_member, tanggal, verivikasi) VALUES ('$nama', '$email', '$password','user.png', 'user.png','$tgl', '1')");    
        if($query){
            $response = new emp();
			$response->response = "Sukses mendaftar!";
            $response->code = 1;
            die(json_encode($response));
        }else{
            $response = new emp();
			$response->response = "Gagal mendaftar!";
            $response->code = 2;
            die(json_encode($response));
        }
        
    }else{
        $response = new emp();
	$response->response = "Email telah tenrdaftar!";
            $response->code = 0;
            die(json_encode($response));
    }   
    mysqli_close($con); 
?>
    