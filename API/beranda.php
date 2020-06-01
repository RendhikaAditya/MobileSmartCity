<?php 
    require_once('koneksi.php');
   // $id = $_GET['info_id'];
    $query = "SELECT * FROM `tb_posting` LIMIT 5";
    $query_wisata = "SELECT * FROM `tb_posting` LIMIT 5";
    $query_budaya = "SELECT * FROM `tb_posting` LIMIT 5";
    $query_acara = "SELECT * FROM `tb_posting` ";
    $query_menu ="SELECT * FROM `tb_kategori`";

    $berita  =mysqli_query($con,$query);
    $menu =mysqli_query($con,$query_menu);
    $wisata =mysqli_query($con,$query_wisata);
    $budaya =mysqli_query($con,$query_budaya);
    $acara =mysqli_query($con,$query_acara);
 
 
if(mysqli_num_rows($menu) > 0 ){
  $response['slider'] = array();  
  while($x = mysqli_fetch_array($berita)){
    $h['id_slider'] = $x['id_posting'];
    $h['caption'] = $x['judul_posting'];
    $h['jenis'] = "Berita";
    $h['slider'] = 'http://franchise.depotmart.id/admin/asset/gambar/'.$x['cover_posting'];
    array_push($response['slider'], $h);
  }
  
  $response['menu_app'] = array();  
  while($w = mysqli_fetch_array($menu)){
    $h1['id_menu_app'] = $w['kategori_id'];
    $h1['title_menu_app'] = $w['kategori_nama'];
    $h1['subtitle_menu_app'] = $w['kategori_nama'];
    $h1['image_menu_app'] = 'http://franchise.depotmart.id/admin/asset/gambar/'.$w['kategori_icon'];
    array_push($response['menu_app'], $h1);
  }
  
  $response['wisata'] = array();  
  while($w1 = mysqli_fetch_array($wisata)){
    $h2['id_konten_app'] = $w1['id_posting'];
    $h2['title_konten_app'] = $w1['judul_posting'];
    $h2['subtitle_konten_app'] = "ini konten wisata";
    $h2['konten_app'] = $w1['isi_posting'];
    $h2['lat'] = $w1['lat'];
    $h2['lng'] = $w1['lng'];
    $h2['image_konten_app'] = 'http://franchise.depotmart.id/admin/asset/gambar/'.$w1['cover_posting'];
    array_push($response['wisata'], $h2);
  }
  
  $response['budaya'] = array();  
  while($w2 = mysqli_fetch_array($budaya)){
    $h3['id_konten_app'] = $w2['id_posting'];
    $h3['title_konten_app'] = $w2['judul_posting'];
    $h3['subtitle_konten_app'] = "ini konten budaya";
    $h3['konten_app'] = $w2['isi_posting'];
    $h3['lat'] = $w2['lat'];
    $h3['lng'] = $w2['lng'];
    $h3['image_konten_app'] = 'http://franchise.depotmart.id/admin/asset/gambar/'.$w2['cover_posting'];
    array_push($response['budaya'], $h3);
  }
  
  $response['acara'] = array();  
  while($w3 = mysqli_fetch_array($acara)){
    $h4['id_konten_app'] = $w3['id_posting'];
    $h4['title_konten_app'] = $w3['judul_posting'];
    $h4['subtitle_konten_app'] = "ini konten wisata";
    $h4['konten_app'] = $w3['isi_posting'];
    $h4['lat'] = $w3['lat'];
    $h4['lng'] = $w3['lng'];
    $h4['image_konten_app'] = 'http://franchise.depotmart.id/admin/asset/gambar/'.$w3['cover_posting'];
    array_push($response['acara'], $h4);
  }
  echo strip_tags(json_encode($response));
 
}else {
  $response["message"]="tidak ada data";
  echo json_encode($response);
}
?>
