<?php 
    require_once('koneksi.php');
    $id = $_POST['id_aduan'];
    $query = "SELECT * FROM `tb_tanggapan` LEFT JOIN tb_member ON tb_tanggapan.id_member=tb_member.id_member WHERE `id_aduan`=$id";
    
    $squry = "SELECT * FROM `tb_aduan` LEFT JOIN tb_member ON tb_aduan.id_member=tb_member.id_member WHERE `id_aduan`=$id";

    $berita  =mysqli_query($con,$query);
    
    $aduan = mysqli_query($con, $squry);
 
if(mysqli_num_rows($berita) > 0 ){
  $response = array();  
  $s= mysqli_fetch_array($aduan);
  while($x = mysqli_fetch_array($berita)){
    $h['id_tanggapan'] = $x['id_tanggapan'];
    $h['isi_tanggapan'] = $x['isi_tanggapan'];
    $h['nama_admin'] = $s['nama_member'];
    $h['tanggal_tanggapan'] = $s['tgl_tanggapan'];
    $h['thumb_admin'] = 'http://franchise.depotmart.id/admin/asset/gambar/'.$x['thumb_member'];
    
    $h['nama_user'] = $x['nama_member'];
    $h['thumb_user'] = 'http://franchise.depotmart.id/admin/asset/gambar/'.$x['thumb_member'];
    $h['verified'] = $s['verivikasi'];
    array_push($response, $h);
    }
  echo strip_tags(json_encode($response));
}else {
  $response["message"]="tidak ada data";
  echo json_encode($response);
}
?>