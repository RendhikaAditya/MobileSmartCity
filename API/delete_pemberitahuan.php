<?php 
    require_once('koneksi.php');
    class emp{}
    $id = $_POST['id_user'];
    $query = mysqli_query($con, "DELETE FROM `tb_tanggapan` WHERE `id_member_a` =$id");
    
    if($query){
        $response = new emp();
    	$response->response = "Pemberitahuan di hapus";
        $response->code = 1;
        die(json_encode($response));
    }else{
        $response = new emp();
    	$response->response = "Gagal Menghapus";
        $response->code = 0;
        die(json_encode($response));
        
    }


  echo json_encode($response);

?>