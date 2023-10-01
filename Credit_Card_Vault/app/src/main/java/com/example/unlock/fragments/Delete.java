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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unlock.R;
import com.example.unlock.tdes_in_cbc;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Delete extends Fragment {
    private Button upd_bttn1;
    private Button rmv_rec1;
    private TextView rtd_tv;
    Spinner s2;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_delete, container, false);
        s2 = (Spinner) view.findViewById(R.id.Spinner02);
        get_title_to_spinner();
        upd_bttn1 = (Button) view.findViewById(R.id.upd_bttn1);
        upd_bttn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                get_title_to_spinner();
            }
        });
        rmv_rec1 = (Button) view.findViewById(R.id.rmv_rec1);
        rmv_rec1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                delete_record();
            }
        });
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                display_rec_to_del(i);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });
        return view;
    }

    private void display_rec_to_del(int rn){
        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_ALARMS);
        File[] files = directory.listFiles();
        String[] result = read_string_from_file(files[rn].getName()).split(",");
        TextView rtd_tv = (TextView) getView().findViewById(R.id.rtd_tv);
        rtd_tv.setText("Record to delete: " + ((char) (34)) + tdes_in_cbc.decrypt_3des_in_cbc(result[0]) + ((char) (34)));
    }

    private void delete_record(){
        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_ALARMS);
        File[] files = directory.listFiles();
        boolean dlt = delete_file(files[s2.getSelectedItemPosition()].getName());
        if (dlt == true){
            Toast.makeText(getActivity(), "Credit Card Deleted Successfully", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity(), "Failed To Delete Credit Card", Toast.LENGTH_LONG).show();
        }
        TextView rtd_tv = (TextView) getView().findViewById(R.id.rtd_tv);
        rtd_tv.setText("");
        get_title_to_spinner();
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
        String[] arraySpinner1 = new String[files.length];
        for (int i = 0; i < files.length; i++) {
            arraySpinner1[i] = cards_in_memory[i][1];
        }
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_item, arraySpinner1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(adapter1);
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

    private boolean delete_file(String filename){
        ContextWrapper contextWrapper = new ContextWrapper(getContext());
        File directory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_ALARMS);
        File txtFile = new File(directory, filename);
        boolean deleted = false;
        try {
            deleted = txtFile.delete();
        } catch (Exception e) {
        }
        return deleted;
    }
}