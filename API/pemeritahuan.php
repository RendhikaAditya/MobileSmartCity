<?php 
    require_once('koneksi.php');
    $id = $_POST['id_user'];
    $query = "SELECT * FROM `tb_tanggapan` WHERE `id_member_a`=$id";

    $berita  =mysqli_query($con,$query);

 
 
if(mysqli_num_rows($berita) > 0 ){
  $response = array();  
  while($x = mysqli_fetch_array($berita)){
    $h['id_pemberitahuan'] = $x['id_tanggapan'];
    $h['id_user'] = $x['id_member_a'];
    $h['isi_pemberitahuan'] = "Seseorang telah menanggapi aduan anda";
    $h['tanggal'] = $x['tgl_tanggapan'];
    array_push($response, $h);
}
  echo strip_tags(json_encode($response));
}else {
  $response["message"]="tidak ada data";
  echo json_encode($response);
}
?>