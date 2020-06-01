package com.ardev.tugasakhir.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.ardev.tugasakhir.BuildConfig;
import com.ardev.tugasakhir.R;
import com.ardev.tugasakhir.fragment.BottomSheetActivateLocation;
import com.ardev.tugasakhir.fragment.BottomSheetActivateStorage;
import com.ardev.tugasakhir.util.PrefManager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dmax.dialog.SpotsDialog;

public class BuatAduan extends BaseActivity implements BottomSheetActivateLocation.BottomSheetListener {

    FloatingActionButton fab_camera, fab_kirim;
    String id_user;
    EditText txt_isi_aduan;
    PrefManager prefManager;
    AlertDialog dialog;
    CardView card_foto_aduan;
    ImageView foto_aduan;
    ImageButton img_btn_close;
    boolean isImageExist = false;

    private static final String TAG = BuatAduan.class.getSimpleName();
    Uri fileUri;
    int PICK_IMAGE_REQUEST = 1;
    public final int SELECT_FILE = 1;
    public final int REQUEST_CAMERA = 0;
    int bitmap_size = 100; // range 1 - 100
    Bitmap bitmap, decoded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buat_aduan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(Html.fromHtml("<b>BUAT ADUAN</b>"));
        findView();
        initView();
        initListener();
        AndroidNetworking.initialize(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (!(txt_isi_aduan.getText().toString().isEmpty()) || isImageExist) {
            new AlertDialog.Builder(this)
                    .setTitle("Perhatian")
                    .setMessage("Apakah anda yakin akan kembali?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        } else {
            finish();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (!(txt_isi_aduan.getText().toString().isEmpty()) || isImageExist) {
            new AlertDialog.Builder(this)
                    .setTitle("Perhatian")
                    .setMessage("Apakah anda yakin akan kembali?")
                    .setCancelable(false)
                    .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("Tidak", null)
                    .show();
        } else {
            finish();
        }
    }

    @Override
    public void findView() {
        fab_camera = findViewById(R.id.fab_camera);
        fab_kirim = findViewById(R.id.fab_kirim);
        txt_isi_aduan = findViewById(R.id.txt_isi_aduan);
        card_foto_aduan = findViewById(R.id.card_foto_aduan);
        foto_aduan = findViewById(R.id.foto_aduan);
        img_btn_close = findViewById(R.id.img_btn_close);
    }

    @Override
    public void initView() {
        prefManager = new PrefManager(getApplicationContext());
        id_user = prefManager.getIdUser();
        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Sedang mengirim aduan...")
                .setCancelable(false)
                .build();
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            fab_camera.show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            fab_camera.hide();
                            BottomSheetActivateStorage bottomSheet = new BottomSheetActivateStorage();
                            bottomSheet.show(getSupportFragmentManager(), "exampleBottomSheet");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    @Override
    public void initListener() {
        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        fab_kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_isi_aduan.getText().length() >= 100) {
                    if (isImageExist) {
                        kirim_aduan_foto(id_user, txt_isi_aduan.getText().toString());
                    } else {
                        kirim_aduan(id_user, txt_isi_aduan.getText().toString());
                    }
                } else {
                    snackBar("Aduan harus lebih dari 100 karakter", R.color.error);
                }
            }
        });
        img_btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card_foto_aduan.setVisibility(View.GONE);
                isImageExist = false;
            }
        });
    }


    private void selectImage() {
        //foto_aduan.setImageResource(0);
        final CharSequence[] items;
        if (Build.VERSION.SDK_INT >= 29) {
            items = new CharSequence[]{"Pilih dari galeri"};
        } else {
            items = new CharSequence[]{"Buka kamera", "Pilih dari galeri"};
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(BuatAduan.this);
        builder.setTitle("Tambahkan foto");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Buka kamera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri();
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Pilih dari galeri")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                }
            }
        });
        builder.setNegativeButton("Batal", null);
        builder.show();
    }

    public Uri getOutputMediaFileUri() {
        //return Uri.fromFile(getOutputMediaFile());
        return FileProvider.getUriForFile(BuatAduan.this, BuildConfig.APPLICATION_ID + ".provider", getOutputMediaFile());
    }

    //menyimpan foto ke direktori internal
    private static File getOutputMediaFile() {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pekalongansmartregency");
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e("Monitoring", "Oops! Failed create Monitoring directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "" + timeStamp + ".jpg");
        return mediaFile;
    }

    //menampilkan foto terpiih
    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        foto_aduan.setImageBitmap(decoded);
        card_foto_aduan.setVisibility(View.VISIBLE);
        isImageExist = true;
    }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        if (width > 5000 || height > 5000) {
            width = width / 2;
            height = height / 2;
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    //menentukan sumber foto
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                try {
                    Log.e("CAMERA", fileUri.getPath());

                    bitmap = BitmapFactory.decodeFile(fileUri.getPath());
                    setToImageView(getResizedBitmap(bitmap, 512));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE && data != null && data.getData() != null) {
                try {
                    // mengambil gambar dari Gallery
                    bitmap = MediaStore.Images.Media.getBitmap(BuatAduan.this.getContentResolver(), data.getData());
                    setToImageView(getResizedBitmap(bitmap, 512));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //mengubah foto ke base64
    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void kirim_aduan(String id_user, String isi_aduan) {
        dialog.show();
        AndroidNetworking.post("http://hotel.depotmart.id/api/buat_aduan.php")
                .addBodyParameter("id_user", id_user)
                .addBodyParameter("isi_aduan", isi_aduan)
                .addBodyParameter("lat", prefManager.getLat())
                .addBodyParameter("lng", prefManager.getLng())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject data) {
                        dialog.dismiss();
                        try {
//                            JSONObject data = new JSONObject(response);
                            String code = data.getString("code");
                            // ngecek node error dari api
                            if (code.equals("1")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(BuatAduan.this);
                                builder.setMessage(data.getString("response"))
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            } else if (code.equals("2")) {
                                // terjadi error dan tampilkan pesan error dari API
//                        dialog.hide();
                                snackBar(data.getString("response"), R.color.error);
                            } else if (code.equals("0")) {
                                // terjadi error dan tampilkan pesan error dari API
//                        dialog.hide();
                                snackBar(data.getString("response"), R.color.error);
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Volley", "kirim Error: " + anError.getMessage());
                        dialog.dismiss();
                        //cek error timeout, noconnection dan network error
                    }
                });
    }
//    private void kirim_aduan(final String id_user, final String isi_aduan){
//        dialog.show();
//        // Tag biasanya digunakan ketika ingin membatalkan request volley
//        String tag_string_req = "req_kirim_aduan";
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                ServerApi.buat_aduan, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d("volley", "kirim aduan Response: " + response.toString());
//                dialog.dismiss();
//                try
//                {
//                    JSONObject data = new JSONObject(response);
//                    String code = data.getString("code");
//                    // ngecek node error dari api
//                    if (code.equals("1")) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(BuatAduan.this);
//                        builder.setMessage(data.getString("response"))
//                                .setCancelable(false)
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        finish();
//                                    }
//                                });
//                        AlertDialog alertDialog = builder.create();
//                        alertDialog.show();
//                    } else if(code.equals("2")) {
//                        // terjadi error dan tampilkan pesan error dari API
////                        dialog.hide();
//                        snackBar(data.getString("response"), R.color.error);
//                    } else if(code.equals("0")) {
//                        // terjadi error dan tampilkan pesan error dari API
////                        dialog.hide();
//                        snackBar(data.getString("response"), R.color.error);
//                    }
//                } catch (JSONException e) {
//                    dialog.dismiss();
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("Volley", "Login Error: " + error.getMessage());
//                dialog.dismiss();
//                //cek error timeout, noconnection dan network error
//                if ( error instanceof TimeoutError || error instanceof NoConnectionError ||error instanceof NetworkError) {
//                    snackBar("Sepertinya ada yang salah dengan koneksi anda", R.color.error);
//                }
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                // kirim parameter ke server
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("id_user", id_user);
//                params.put("isi_aduan", isi_aduan);
//                params.put("lat", prefManager.getLat());
//                params.put("lng", prefManager.getLng());
//
//                return params;
//            }
//        };
//        // menggunakan fungsi volley adrequest yang kita taro di appcontroller
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//    }

    private void kirim_aduan_foto(final String id_user, final String isi_aduan) {
        dialog.show();
        AndroidNetworking.post("http://hotel.depotmart.id/api/buat_aduan.php")
                .addBodyParameter("id_user",id_user)
                .addBodyParameter("isi_aduan", isi_aduan)
                .addBodyParameter("foto_aduan", getStringImage(decoded))
                .addBodyParameter("lat", prefManager.getLat())
                .addBodyParameter("lng", prefManager.getLng())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject data) {
                        dialog.dismiss();
                        try {
//                            JSONObject data = new JSONObject(response);
                            String code = data.getString("code");
                            // ngecek node error dari api
                            if (code.equals("1")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(BuatAduan.this);
                                builder.setMessage(data.getString("response"))
                                        .setCancelable(false)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        });
                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();
                            } else if (code.equals("2")) {
                                // terjadi error dan tampilkan pesan error dari API
//                        dialog.hide();
                                snackBar(data.getString("response"), R.color.error);
                            } else if (code.equals("0")) {
                                // terjadi error dan tampilkan pesan error dari API
//                        dialog.hide();
                                snackBar(data.getString("response"), R.color.error);
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("Volley", "Login Error: " + anError.getMessage());
                        dialog.dismiss();
                    }
                });
    }
//    private void kirim_aduan_foto(final String id_user, final String isi_aduan) {
//        dialog.show();
//        // Tag biasanya digunakan ketika ingin membatalkan request volley
//        String tag_string_req = "req_kirim_aduan";
//
//        StringRequest strReq = new StringRequest(Request.Method.POST,
//                ServerApi.buat_aduan, new Response.Listener<String>() {
//
//            @Override
//            public void onResponse(String response) {
//                Log.d("volley", "kirim aduan Response: " + response.toString());
//                dialog.dismiss();
//                try {
//                    JSONObject data = new JSONObject(response);
//                    String code = data.getString("code");
//                    // ngecek node error dari api
//                    if (code.equals("1")) {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(BuatAduan.this);
//                        builder.setMessage(data.getString("response"))
//                                .setCancelable(false)
//                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialogInterface, int i) {
//                                        finish();
//                                    }
//                                });
//                        AlertDialog alertDialog = builder.create();
//                        alertDialog.show();
//                    } else if (code.equals("2")) {
//                        // terjadi error dan tampilkan pesan error dari API
////                        dialog.hide();
//                        snackBar(data.getString("response"), R.color.error);
//                    } else if (code.equals("0")) {
//                        // terjadi error dan tampilkan pesan error dari API
////                        dialog.hide();
//                        snackBar(data.getString("response"), R.color.error);
//                    }
//                } catch (JSONException e) {
//                    dialog.dismiss();
//                    e.printStackTrace();
//                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//            }
//        }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.e("Volley", "Login Error: " + error.getMessage());
//                dialog.dismiss();
//                //cek error timeout, noconnection dan network error
//                if (error instanceof TimeoutError || error instanceof NoConnectionError || error instanceof NetworkError) {
//                    snackBar("Sepertinya ada yang salah dengan koneksi anda", R.color.error);
//                }
//            }
//        }) {
//            @Override
//            protected Map<String, String> getParams() {
//                // kirim parameter ke server
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("id_user", id_user);
//                params.put("isi_aduan", isi_aduan);
//                params.put("foto_aduan", getStringImage(decoded));
//                params.put("lat", prefManager.getLat());
//                params.put("lng", prefManager.getLng());
//                return params;
//            }
//        };
//        // menggunakan fungsi volley adrequest yang kita taro di appcontroller
//        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
//    }

    private void snackBar(String pesan, int warna) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), pesan, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), warna));
        snackbar.show();
    }

    @Override
    public void onButtonClicked(String text) {

    }
}
