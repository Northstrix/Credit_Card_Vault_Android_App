package com.example.unlock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {
    protected EditText m_password;
    private Button unlock_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(getApplicationContext(), read_string_from_file("master_password"), Toast.LENGTH_SHORT).show();
        if (read_string_from_file("master_password") == "-1"){
            modify_interface_for_set_master_password();
        }
        unlock_button = (Button) findViewById(R.id.unlock_button);
        unlock_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                if (read_string_from_file("master_password") == "-1"){
                    set_master_password();
                }
                else{
                    unlock_the_app();
                }

            }
        });

    }

    public void set_master_password(){
        m_password = (EditText) findViewById(R.id.m_password);
        byte[] hashedBytes = new byte[64];
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] bytes = m_password.getText().toString().getBytes();
            hashedBytes = digest.digest(bytes);
        }
        catch (NoSuchAlgorithmException e) {
        // Handle the exception
        e.printStackTrace();
        }
        tdes_in_cbc.set_key(hashedBytes);
        StringBuilder builder = new StringBuilder();
        for (int b = 20; b < 64; b ++) {
            builder.append(String.format("%02x", b));
        }
        String encrypted_master_password_hash = tdes_in_cbc.encrypt_3des_in_cbc(builder.toString());
        write_to_file("master_password", encrypted_master_password_hash);
        get_to_tabs();
    }
    public void unlock_the_app(){
        m_password = (EditText) findViewById(R.id.m_password);
        byte[] hashedBytes = new byte[64];
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] bytes = m_password.getText().toString().getBytes();
            hashedBytes = digest.digest(bytes);
        }
        catch (NoSuchAlgorithmException e) {
            // Handle the exception
            e.printStackTrace();
        }
        tdes_in_cbc.set_key(hashedBytes);
        StringBuilder builder = new StringBuilder();
        for (int b = 20; b < 64; b ++) {
            builder.append(String.format("%02x", b));
        }
        if (builder.toString().equals(tdes_in_cbc.decrypt_3des_in_cbc(read_string_from_file("master_password")))){
            Toast.makeText(getApplicationContext(), "App Unlocked Successfully", Toast.LENGTH_SHORT).show();
            get_to_tabs();
        }
        else{
            Toast.makeText(getApplicationContext(), "Wrong Password", Toast.LENGTH_SHORT).show();
        }
    }
    public void get_to_tabs(){
        Intent intent = new Intent(this, tabs.class);
        startActivity(intent);
    }

    public void modify_interface_for_set_master_password(){
        m_password = (EditText) findViewById(R.id.m_password);
        m_password.setHint("Set The Master Password");
        unlock_button = (Button) findViewById(R.id.unlock_button);
        unlock_button.setText("Set");
    }

    public void write_to_file(String filename, String text){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File txtFile = new File(directory, filename + ".ccvr");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(txtFile);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            osw.write(text);
            osw.flush();
            osw.close();
            fos.close();
        } catch (Exception e) {
            // on below line handling the exception.
            e.printStackTrace();
        }
    }
    public String read_string_from_file(String filename){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File txtFile = new File(directory, filename + ".ccvr");
        // on below line creating a string builder.
        StringBuilder text = new StringBuilder();
        boolean empty_fl = false;
        try {
            // on below line creating and initializing buffer reader.
            BufferedReader br = new BufferedReader(new FileReader(txtFile));
            // on below line creating a string variable/
            String line;
            // on below line setting the data to text
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
            // on below line handling the exception
        } catch (Exception e) {
            empty_fl = true;
        }
        //Toast.makeText(contextWrapper, text, Toast.LENGTH_SHORT).show();
        if (empty_fl == false)
            return text.toString();
        else
            return "-1";
    }

}