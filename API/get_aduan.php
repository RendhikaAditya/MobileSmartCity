
<?php 
    require_once('koneksi.php');
    $id = $_POST['id_user'];
    $query = "SELECT * FROM `tb_aduan` LEFT JOIN tb_member ON tb_aduan.id_member=tb_member.id_member";

    $berita  =mysqli_query($con,$query);

 
 
if(mysqli_num_rows($berita) > 0 ){
  $response = array();  
 while($x = mysqli_fetch_array($berita)){
    $h['id_aduan'] = $x['id_aduan'];
    $h['isi_aduan'] = $x['isi_aduan'];
    $h['status_aduan'] = $x['status_aduan'];
    $h['foto_aduan'] = 'http://franchise.depotmart.id/apia/gambar/'.$x['foto_aduan'];
    $h['lng'] = $x['lng'];
    $h['lat'] = $x['lat'];
    $h['tanggal_aduan'] = $x['tanggal'];
    $h['nama_user'] = $x['nama_member'];
    $h['thumb_user'] = $x['thumb_member'];
    $h['id_user'] = $x['id_member'];
    $h['tanggapan'] = $x['tanggapan_member'];
    $h['verified'] = $x['verivikasi'];
    $h['response']="berhasil";
    array_push($response, $h);
}
  echo strip_tags(json_encode($response));
}else {
  $response["message"]="tidak ada data";
  echo json_encode($response);
}
?>
