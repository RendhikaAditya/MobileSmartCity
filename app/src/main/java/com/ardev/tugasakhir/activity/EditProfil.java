package com.ardev.tugasakhir.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.ardev.tugasakhir.R;
import com.ardev.tugasakhir.server.ServerApi;
import com.ardev.tugasakhir.util.AppController;
import com.ardev.tugasakhir.util.PrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class EditProfil extends BaseActivity {

    Bundle bundle;
    String nama_user,pass_user, bio_user, telp_user, alamat_user, nik_user, jk_user, spin_jk_user;
    EditText txtnama_user, txtbio_user, txtalamat_user, txtnik_user, txttelp_user, txtPass;
    RadioGroup radjk_user;
    RadioButton jk_userl, jk_userp;
    Button btn_simpan, btn_batal;
    ImageView thumb_user;
    RelativeLayout edit_thumb;

    AlertDialog dialog;

    int PICK_IMAGE_REQUEST=1;
    int bitmap_size = 60;
    Bitmap bitmap, decoded;
    int sukses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle(Html.fromHtml("<b>EDIT PROFIL</b>"));
        findView();
        initView();
        initListener();
        AndroidNetworking.initialize(this);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    @Override
    public void findView() {
        txtnama_user = findViewById(R.id.nama_user);
        txtPass=findViewById(R.id.pass_user);
        txtbio_user = findViewById(R.id.bio_user);
        txtalamat_user = findViewById(R.id.alamat_user);
        txtnik_user = findViewById(R.id.nik_user);
        txttelp_user = findViewById(R.id.telp_user);
        radjk_user = findViewById(R.id.jk_user);
        jk_userl = findViewById(R.id.jk_userl);
        jk_userp = findViewById(R.id.jk_userp);
        btn_simpan = findViewById(R.id.btn_simpan);
        btn_batal = findViewById(R.id.btn_batal);
        thumb_user = findViewById(R.id.thumb_user);
        edit_thumb = findViewById(R.id.edit_thumb);
    }

    @Override
    public void initView() {
        bundle = getIntent().getExtras();
        //snackBar(bundle.getString("id_user"), R.color.info);
        nama_user = bundle.getString("nama_user");
        pass_user = bundle.getString("pass_user");
        bio_user = bundle.getString("bio_user");
        telp_user = bundle.getString("telp_user");
        alamat_user = bundle.getString("alamat_user");
        nik_user = bundle.getString("nik_user");
        jk_user = bundle.getString("jk_user");

        txtnama_user.setText(nama_user);
        txtPass.setText(pass_user);
        txtbio_user.setText(bio_user);
        txtalamat_user.setText(alamat_user);
        txtnik_user.setText(nik_user);
        txttelp_user.setText(telp_user);

        if (jk_user.equals("Laki-laki")){
            jk_userl.setChecked(true);
            spin_jk_user = "Laki-laki";
        }else if (jk_user.equals("Perempuan")){
            jk_userp.setChecked(true);
            spin_jk_user = "Perempuan";
        }else{
            spin_jk_user = "Laki-laki";
            jk_userl.setChecked(true);
        }

        Glide.with(getApplicationContext())
                .load(bundle.getString("thumb_user"))
                .placeholder(R.drawable.img_profile)
                .into(thumb_user);

        dialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Sedang menyimpan profil...")
                .setCancelable(false)
                .build();
    }

    @Override
    public void initListener() {
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtnama_user.getText().toString().isEmpty()){
                    txtnama_user.setError("Harus diisi");
                }else if (txtbio_user.getText().toString().isEmpty()){
                    txtbio_user.setError("Harus diisi");
                }else if (txtalamat_user.getText().toString().isEmpty()){
                    txtalamat_user.setError("Harus diisi");
                }else if (txtnik_user.getText().toString().isEmpty()){
                    txtnik_user.setError("Harus diisi");
                }else if (txttelp_user.getText().toString().isEmpty()){
                    txttelp_user.setError("Harus diisi");
                }else{
                    if(decoded==null){
                        simpan();
//                        uploadImage();
                    }else{
                        uploadImage();
                        simpan();
                    }
                }
            }
        });

        btn_batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        radjk_user.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (jk_userl.isChecked()){
                    spin_jk_user = "Laki-laki";
                }else if(jk_userp.isChecked()){
                    spin_jk_user = "Perempuan";
                }
            }
        });

        edit_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

    }



    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);

        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {
        AndroidNetworking.post("http://hotel.depotmart.id/api/edit_image.php")
                .addBodyParameter("gambar", getStringImage(decoded))
                .addBodyParameter("id_user", bundle.getString("id_user"))
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            sukses = response.getInt("success");
                            if (sukses == 1) {
                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            } else {

                                Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("Error reg", "error : "+anError);
                    }
                });
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void kosong() {
        thumb_user.setImageResource(0);
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));
        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        thumb_user.setImageBitmap(decoded);
    }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void simpan() {
        dialog.show();

        // Tag biasanya digunakan ketika ingin membatalkan request volley
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                ServerApi.update_profil, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("volley", "Login Response: " + response.toString());

                try
                {
                    JSONObject data = new JSONObject(response);
                    String code = data.getString("code");
                    dialog.dismiss();
                    // ngecek node error dari api
                    if (code.equals("1")) {
                        finish();
                    } else if(code.equals("0")) {
                        // terjadi error dan tampilkan pesan error dari API
//                        dialog.hide();
                        snackBar(data.getString("response"), R.color.error);
                    }else{
                        snackBar("Sepertinya ada yang salah dengan koneksi anda", R.color.error);
                    }
                } catch (JSONException e) {
                    // JSON error
                    dialog.dismiss();
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", "Login Error: " + error.getMessage());
                dialog.dismiss();
                //cek error timeout, noconnection dan network error
                if ( error instanceof TimeoutError || error instanceof NoConnectionError ||error instanceof NetworkError) {
                    snackBar("Sepertinya ada yang salah dengan koneksi anda", R.color.error);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // kirim parameter ke server
                Map<String, String> params = new HashMap<String, String>();
                params.put("nama_user", txtnama_user.getText().toString());
                params.put("pass_user", txtPass.getText().toString());
                params.put("bio_user", txtbio_user.getText().toString());
                params.put("nik_user", txtnik_user.getText().toString());
                params.put("telp_user", txttelp_user.getText().toString());
                params.put("alamat_user", txtalamat_user.getText().toString());
                params.put("jk_user", spin_jk_user);
                params.put("id_user", bundle.getString("id_user"));

                return params;
            }
        };
        // menggunakan fungsi volley adrequest yang kita taro di appcontroller
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void snackBar(String pesan, int warna){
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), pesan, Snackbar.LENGTH_LONG)
                .setAction("Action", null);
        View sbView = snackbar.getView();
        sbView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), warna));
        snackbar.show();
    }
}
