<?php 
    require_once('koneksi.php');
    class emp{}
	$id = $_POST['id_user'];
	$isi = $_POST['isi_aduan'];
	$foto = $_POST['foto_aduan'];
	$lat = $_POST['lat'];
	$lng = $_POST['lng'];
	$a= "aktif";
	
    $tgl = date('d M Y');
    $newname = date('YmdHis',time()).mt_rand().'.png';
    file_put_contents("gambar/".$newname, base64_decode($foto));
            
                $query = mysqli_query($con, "INSERT INTO `tb_aduan` (`id_member`, `isi_aduan`, `foto_aduan`, `lat`, `lng`, `tanggal`,`status_aduan`) VALUES ('$id', '$isi', '$newname', '$lat', '$lng',  '$tgl', '$a')");    
                
            if($query){
                
                $response = new emp();
    			$response->response = "Aduan berhasil dikirim. Aduan akan diperiksa oleh administrator sebelum ditampilkan ke publik";
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
    