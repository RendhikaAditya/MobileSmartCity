<?php 
    require_once('koneksi.php');
    class emp{}
	$id = $_POST['id_user'];
	$ida = $_POST['id_user_a'];
	$idaduan = $_POST['id_aduan'];
	$isi = $_POST['isi_tanggapan'];
    $tgl = date('d M Y');
    
    
            
                $query = mysqli_query($con, "INSERT INTO `tb_tanggapan` (`id_member`, `id_member_a`, `id_aduan`, `isi_tanggapan`, `tgl_tanggapan`) VALUES ('$id', '$ida', '$idaduan', '$isi', '$tgl');");    
            if($query){
                
                $response = new emp();
    			$response->response = "Taggapan Berhasil di buat";
                $response->code = 1;
                die(json_encode($response));
            }else{
                $response = new emp();
    			$response->response = "Gagal Membuat!";
                $response->code = 0;
                die(json_encode($response));
            }
        
    mysqli_close($con); 
?>
    