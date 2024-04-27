package com.example.b1_prak5_13120220022;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private EditText txtStb,txtNama,txtAngkatan;
    private Button btnSimpan,btnTampil;
    private DbHelper dbHelper;
    private Mahasiswa mhs;
    private Intent intentEdit;

    private void clearText(){
        txtStb.setText("");
        txtNama.setText("");
        txtAngkatan.setText("");
        intentEdit = null;
        txtStb.requestFocus();
    }

    private void simpanData() {
        Mahasiswa mhs = new Mahasiswa(
                txtStb.getText().toString(),
                txtNama.getText().toString(),
                Integer.parseInt(txtAngkatan.getText().toString())
        );
        dbHelper.insertData(dbHelper.getWritableDatabase(), mhs);
        Toast.makeText(this, "Data tersimpan...", Toast.LENGTH_LONG).show();
        clearText();
    }

    private void editData() {
        Mahasiswa mhs = new Mahasiswa(
                txtStb.getText().toString(),
                txtNama.getText().toString(),
                Integer.parseInt(txtAngkatan.getText().toString())
        );
        dbHelper.editData(dbHelper.getWritableDatabase(), mhs, intentEdit.getStringExtra("stb"));
        Toast.makeText(this, "Edit Data berhasil...", Toast.LENGTH_LONG).show();
        clearText();
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1){
        intentEdit = data;
        txtStb.setText(data.getStringExtra("stb"));
        txtNama.setText(data.getStringExtra("nama"));
        txtAngkatan.setText(String.valueOf(data.getIntExtra("angkatan",0)));
    }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        dbHelper = new DbHelper(this);

        txtStb = findViewById(R.id.txt_stb);
        txtNama = findViewById(R.id.txt_nama);
        txtAngkatan = findViewById(R.id.txt_angkatan);
        btnSimpan = findViewById(R.id.btn_simpan);
        btnTampil = findViewById(R.id.btn_tampil);

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intentEdit == null) {
                    simpanData();
                } else {
                    editData();
                }
            }
        });

        btnTampil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentEdit = null;
                Intent intent = new Intent(getApplicationContext(), TampilActivity.class);
                startActivityForResult(intent, 1);
                dbHelper.close();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    }
