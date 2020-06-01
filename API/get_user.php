<?php 
    require_once('koneksi.php');
    $id = $_POST['id_user'];
    $query = "SELECT * FROM `tb_member` WHERE `id_member`=$id";

    $berita  =mysqli_query($con,$query);

 
 
if(mysqli_num_rows($berita) > 0 ){
  $response = array();  
  $x = mysqli_fetch_array($berita);
    $h['id_user'] = $x['id_member'];
    if($x['nik_member']==null){
        $h['nik_user'] = "Nik Belum Diisi";
    }else{
        $h['nik_user'] = $x['nik_member'];
    }
    $h['nama_user'] = $x['nama_member'];
    if($x['gender_member']==null){
        $h['jk_user'] = "Jenis Kelamin Belum Dipilih";
    }else{
        $h['jk_user'] = $x['gender_member'];
    }
    $h['email_user'] = $x['email_member'];
    if($x['notelp_member']==null){
        $h['telp_user'] = "Nomor Belum Diisi";
    }else{
        $h['telp_user'] = $x['notelp_member'];
    }
    $h['foto_user'] = 'http://franchise.depotmart.id/admin/asset/gambar/'.$x['foto_member'];
    $h['thumb_user'] = 'http://franchise.depotmart.id/admin/asset/gambar/'.$x['thumb_member'];
    if($x['bio_member']==null){
        $h['bio_user'] = "Pengguna Padang Smartregency";
    }else{
        $h['bio_user'] = $x['bio_member'];
    }
    if($x['alamat_member']==null){
        $h['alamat_user'] = "Alamat Belum Diisi";
    }else{
        $h['alamat_user'] = $x['alamat_member'];
    }
    $h['aktif'] = "1";
    $h['verified'] = "0";
    $h['aduan'] = "0";
    $h['tanggapan'] = "0";
    $h['tanggal'] = $x['tanggal'];
    array_push($response, $h);

  echo strip_tags(json_encode($response));
}else {
  $response["message"]="tidak ada data";
  echo json_encode($response);
}
?>
