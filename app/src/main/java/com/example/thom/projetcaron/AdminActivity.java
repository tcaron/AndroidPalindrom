package com.example.thom.projetcaron;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;

public class AdminActivity extends AppCompatActivity {

    private Button save;
    private EditText myChain;
    private CheckBox is_pal;
    private Button quit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        save = (Button) findViewById(R.id.save_new);
        myChain = (EditText) findViewById(R.id.txt_to_save);
        is_pal = (CheckBox) findViewById(R.id.must_a_pal);
        quit = (Button) findViewById(R.id.admin_return);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    OutputStream o_npal = getApplicationContext().openFileOutput("nonpalindromes.txt", MODE_APPEND);
                    OutputStreamWriter w_npal = new OutputStreamWriter(o_npal, StandardCharsets.ISO_8859_1);

                    OutputStream o_pal = getApplicationContext().openFileOutput("palindromes.txt", MODE_APPEND);
                    OutputStreamWriter w_pal = new OutputStreamWriter(o_pal, StandardCharsets.ISO_8859_1);

                    if ( myChain.getText().toString().isEmpty()){
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_txt_to_save),Toast.LENGTH_SHORT).show();
                    }
                    else {

                        if (is_pal.isChecked()) {
                            BufferedWriter p_writer = new BufferedWriter(w_pal);
                            // on nettoie la chaine de caractère
                            String chainClean = Normalizer.normalize(myChain.getText().toString(), Normalizer.Form.NFD);
                            chainClean =chainClean.replaceAll("[^a-zA-Z]","").toLowerCase();
                            StringBuilder reverseChain = new StringBuilder(chainClean).reverse();
                            String reverse = reverseChain.toString();
                            System.out.println(reverse);
                            System.out.println(chainClean);

                            if (chainClean.equals(reverse)){

                                try {
                                    p_writer.newLine();
                                    p_writer.write(myChain.getText().toString());
                                    p_writer.flush();
                                    o_pal.close();
                                    w_pal.close();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                myChain.setText("");
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.correctly_add),Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.not_a_palindrom),Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            BufferedWriter writer = new BufferedWriter(w_npal);
                            try {
                                writer.newLine();
                                writer.write(myChain.getText().toString());
                                writer.flush();
                                o_npal.close();
                                w_npal.close();
                                myChain.setText("");
                                Toast.makeText(getApplicationContext(), getResources().getString(R.string.correctly_add),Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // return super.onCreateOptionsMenu(options_menu.xmll);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.random_p:
                break;
            case R.id.about:
                final Dialog dialog = new Dialog(AdminActivity.this);
                dialog.setContentView(R.layout.about);
                Button quit = dialog.findViewById(R.id.quit_modal);
                quit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.is_palindrome:
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.admin:
                break;
        }
        return true;
    }
}
