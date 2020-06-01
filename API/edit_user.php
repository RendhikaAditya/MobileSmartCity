<?php 
    require_once('koneksi.php');
    class emp{}
	$nama = $_POST['nama_user'];
    $bio =$_POST['bio_user'];
    $nik = $_POST['nik_user'];
    $telp = $_POST['telp_user'];
    $alamat = $_POST['alamat_user'];
    $jk = $_POST['jk_user'];

        $id = $_POST['id_user'];
        $query = mysqli_query($con, "UPDATE tb_member SET `nama_member`='$nama', `nik_member`='$nik',`gender_member`='$jk',`notelp_member`='$telp',`bio_member`='$bio',`alamat_member`='$alamat' WHERE `id_member`='$id'");    
        if($query){
            $response = new emp();
			$response->response = "Sukses Edit!";
            $response->code = 1;
            die(json_encode($response));
        }else{
            $response = new emp();
			$response->response = "Gagal Edit!";
            $response->code = 0;
            die(json_encode($response));
        }
        
  
    mysqli_close($con); 
?>
    