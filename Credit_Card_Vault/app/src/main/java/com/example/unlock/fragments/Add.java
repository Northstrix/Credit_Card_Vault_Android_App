package com.example.unlock.fragments;

import android.content.ContextWrapper;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.unlock.R;
import com.example.unlock.tdes_in_cbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.util.Random;

public class Add extends Fragment {
    private Button add_button;
    private Button clear_bt;
    private EditText title_input;
    private EditText crd_nmbr_input;
    private EditText expiry_input;
    private EditText cvn_input;
    private EditText crdhldr_input;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add, container, false);
        title_input = (EditText) view.findViewById(R.id.title_input);
        crd_nmbr_input = (EditText) view.findViewById(R.id.crd_nmbr_input);
        expiry_input = (EditText) view.findViewById(R.id.expiry_input);
        cvn_input = (EditText) view.findViewById(R.id.cvn_input);
        crdhldr_input = (EditText) view.findViewById(R.id.crdhldr_input);
        add_button = (Button) view.findViewById(R.id.add_button);
        clear_bt = (Button) view.findViewById(R.id.clear_bt);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_cr_card();
            }
        });
        clear_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear_fields();
            }
        });
        return view;
    }

    private void add_cr_card(){
        String ttl = title_input.getText().toString();
        String card_number = crd_nmbr_input.getText().toString();
        card_number = card_number.replace(" ", "");
        String expires_at = expiry_input.getText().toString();
        String cvn = cvn_input.getText().toString();
        String cardh_name = crdhldr_input.getText().toString();
        if(TextUtils.isEmpty(ttl) || TextUtils.isEmpty(card_number) || TextUtils.isEmpty(expires_at) || TextUtils.isEmpty(cvn) || TextUtils.isEmpty(cardh_name)){
            Toast.makeText(getActivity(), "Fill All Fields", Toast.LENGTH_LONG).show();
        }
        else {
            String id = generate_unique_random_id();
            write_to_file(id, tdes_in_cbc.encrypt_3des_in_cbc(ttl) + "," + tdes_in_cbc.encrypt_3des_in_cbc(card_number) + "," + tdes_in_cbc.encrypt_3des_in_cbc(expires_at) + "," + tdes_in_cbc.encrypt_3des_in_cbc(cvn) + "," + tdes_in_cbc.encrypt_3des_in_cbc(cardh_name));
            Toast.makeText(getActivity(), "Credit Card Added Successfully", Toast.LENGTH_LONG).show();
            clear_fields();
        }
    }

    private String generate_unique_random_id(){
        String id;
        final Random myRandom = new Random();
        char[] id_array = new char[24];
        int gm;
        for (int i = 0; i < 24; i++) {
            gm = myRandom.nextInt(3);
            if (gm == 0)
                id_array[i] = ((char)(65 + myRandom.nextInt(26)));
            if (gm == 1)
                id_array[i] = ((char)(97 + myRandom.nextInt(26)));
            if (gm == 2)
                id_array[i] = ((char)(48 + myRandom.nextInt(9)));
        }
        id = String.valueOf(id_array);
        while (read_string_from_file(id) != "-1"){ // Make sure that this id isn't already used
            for (int i = 0; i < 24; i++) {
                gm = myRandom.nextInt(3);
                if (gm == 0)
                    id_array[i] = ((char)(65 + myRandom.nextInt(26)));
                if (gm == 1)
                    id_array[i] = ((char)(97 + myRandom.nextInt(26)));
                if (gm == 2)
                    id_array[i] = ((char)(48 + myRandom.nextInt(9)));
            }
            id = String.valueOf(id_array);
        }
        return id;
    }

    private void clear_fields(){
        title_input.setText("");
        crd_nmbr_input.setText("");
        expiry_input.setText("");
        cvn_input.setText("");
        crdhldr_input.setText("");
    }

    public void write_to_file(String filename, String text){
        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_ALARMS);
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
        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_ALARMS);
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