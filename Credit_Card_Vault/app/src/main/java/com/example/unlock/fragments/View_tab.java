package com.example.unlock.fragments;

import android.content.ContextWrapper;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.unlock.R;
import com.example.unlock.tdes_in_cbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Random;

public class View_tab extends Fragment {
    private ImageView imgv;
    View view;
    private String[] arraySpinner;
    private Button upd_bttn;
    private Button chng_bckg;
    Spinner s;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_tab, container, false);
        imgv = (ImageView) view.findViewById(R.id.imgv);
        s = (Spinner) view.findViewById(R.id.Spinner01);
        change_picture();
        get_title_to_spinner();
        upd_bttn = (Button) view.findViewById(R.id.upd_bttn);
        upd_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                get_title_to_spinner();
            }
        });
        chng_bckg = (Button) view.findViewById(R.id.chang_pctr);
        chng_bckg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                change_picture();
            }
        });
        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                display_credit_card(i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        return view;
    }

    protected void display_credit_card(int card){
        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_ALARMS);
        File[] files = directory.listFiles();
        String[] result = read_string_from_file(files[card].getName()).split(",");
        String unformatted_card_number = tdes_in_cbc.decrypt_3des_in_cbc(result[1]);
        TextView card_number_tv = (TextView) getView().findViewById(R.id.card_number_tv);
        if (unformatted_card_number.length() > 15){
            StringBuilder formatted_card_number = new StringBuilder();
            for (int i = 0; i < 16; i++){
                if (i % 4 == 0 && i < 14 && i > 1){
                    formatted_card_number.append("  ");
                }
                formatted_card_number.append(unformatted_card_number.charAt(i));
            }
            card_number_tv.setText(formatted_card_number.toString());
        }
        else{
            card_number_tv.setText(unformatted_card_number);
        }

        String valid_thru = tdes_in_cbc.decrypt_3des_in_cbc(result[2]);
        String cvn = tdes_in_cbc.decrypt_3des_in_cbc(result[3]);
        String vth_cvn = "VALID THRU:" + valid_thru + "     CVN:" + cvn;
        TextView vth_tv = (TextView) getView().findViewById(R.id.vth_tv);
        vth_tv.setText(vth_cvn);
        String cardholder = tdes_in_cbc.decrypt_3des_in_cbc(result[4]);
        TextView cardholder_nm_tv = (TextView) getView().findViewById(R.id.cardholder_nm_tv);
        cardholder_nm_tv.setText(cardholder);
    }

    private void get_title_to_spinner(){
        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_ALARMS);
        File[] files = directory.listFiles();
        String[][] cards_in_memory = new String[files.length][2];
        for (int i = 0; i < files.length; i++)
        {
            cards_in_memory[i][0] = files[i].getName();
        }
        for (int i = 0; i < files.length; i++)
        {
            String[] result = read_string_from_file(cards_in_memory[i][0]).split(",");
            cards_in_memory[i][1] = tdes_in_cbc.decrypt_3des_in_cbc(result[0]);
        }
        arraySpinner = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            arraySpinner[i] = cards_in_memory[i][1];
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, arraySpinner);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
    }

    private String read_string_from_file(String filename){
        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_ALARMS);
        File txtFile = new File(directory, filename);
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

    public void change_picture(){
        final Random myRandom = new Random();
        int lock_scr = myRandom.nextInt(7);
        if (lock_scr == 0)
            imgv.setImageResource(R.drawable.atlanta);
        if (lock_scr == 1)
            imgv.setImageResource(R.drawable.dallas);
        if (lock_scr == 2)
            imgv.setImageResource(R.drawable.field);
        if (lock_scr == 3)
            imgv.setImageResource(R.drawable.minneapolis);
        if (lock_scr == 4)
            imgv.setImageResource(R.drawable.rock);
        if (lock_scr == 5)
            imgv.setImageResource(R.drawable.santiago);
        if (lock_scr == 6)
            imgv.setImageResource(R.drawable.tel_aviv);
    }
}