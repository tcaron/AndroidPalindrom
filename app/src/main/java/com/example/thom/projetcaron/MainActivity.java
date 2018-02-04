package com.example.thom.projetcaron;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Random;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static android.widget.TextView.BufferType.SPANNABLE;

public class MainActivity extends AppCompatActivity {

    private Button btn_reverse;
    private Button btn_clean;

    private EditText myChain;
    private TextView clean_txt;
    private TextView reverse_txt;

    private ProgressBar loading_bar;

    private String path = Environment.getExternalStorageDirectory()+"/projetCARON/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        String[] files = fileList();
        if (files.length == 0){
            copyAssets();
        }
        btn_clean = (Button) findViewById(R.id.btn_clean);
        btn_reverse = (Button) findViewById(R.id.btn_reverse);

        myChain = (EditText) findViewById(R.id.txt_to_set);
        clean_txt = (TextView) findViewById(R.id.clean_text);
        reverse_txt = (TextView) findViewById(R.id.reverse_text);

        loading_bar = (ProgressBar) findViewById(R.id.loading_bar);
        btn_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chainClean;

                if ( myChain.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_field),Toast.LENGTH_SHORT).show();
                }
                else{
                    chainClean = Normalizer.normalize(myChain.getText().toString(), Normalizer.Form.NFD);
                    chainClean =chainClean.replaceAll("[^a-zA-Z]","").toLowerCase();
                    clean_txt.setText(chainClean);
                }
            }
        });

        btn_reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clean_txt.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_clean),Toast.LENGTH_SHORT).show();
                }
                else{
                    StringBuilder reverse_chain = new StringBuilder();
                    for (int i = clean_txt.getText().toString().length() - 1; i >= 0; i--) {
                        reverse_chain.append(clean_txt.getText().toString().charAt(i));
                    }
                    reverse_txt.setText(reverse_chain.toString());

                }
            }
        });


        myChain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                clean_txt.setText("");
                reverse_txt.setText("");
                loading_bar.setProgress(0);
            }

            @Override
            public void afterTextChanged(Editable editable) {

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

        switch (id){
            case R.id.random_p :
                ArrayList<String> list= new ArrayList<String>();
                int i = 0;
                int j = 0;
                String pal ;
                String not_pal;
                try {
                    InputStream in_pal = getApplicationContext().openFileInput("palindromes.txt");
                    InputStreamReader inr_pal = new InputStreamReader(in_pal,StandardCharsets.ISO_8859_1);
                    InputStream in_npal = getApplicationContext().openFileInput("nonpalindromes.txt");
                    InputStreamReader inr_npal = new InputStreamReader(in_npal,StandardCharsets.ISO_8859_1);
                    BufferedReader pal_br = new BufferedReader(inr_pal);
                    BufferedReader not_pal_br = new BufferedReader(inr_npal);


                    while ((pal = pal_br.readLine()) != null){list.add(pal); i++;} pal_br.close();
                    while ((not_pal = not_pal_br.readLine()) != null){list.add(not_pal);j++;} not_pal_br.close();
                    myChain.setText(list.get((new Random()).nextInt(list.size()-1)));
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_files), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.about:
                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.about);
                Button quit = dialog.findViewById(R.id.quit_modal);
                quit.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                break;
            case R.id.is_palindrome :
                break;

            case R.id.admin:
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private Handler handler;
    public void isPalindrome (View view) throws InterruptedException {
         handler = new Handler();

               final SpannableString reverse_chain = new SpannableString(reverse_txt.getText().toString());
               final SpannableString clean_chain = new SpannableString(clean_txt.getText().toString());
               final BackgroundColorSpan greenBg = new BackgroundColorSpan(Color.GREEN);
               final BackgroundColorSpan redBg = new BackgroundColorSpan(Color.RED);
                final int length = reverse_txt.getText().toString().length();
                loading_bar.setMax(length);
                int countr =0;
               for (int i = 0; i < length; i++) {
                   final int index = i;
                       if (reverse_chain.charAt(i) == clean_chain.charAt(i)) {
                      handler.postDelayed(new Runnable() {
                          @Override
                          public void run() {
                              reverse_chain.setSpan(greenBg,0,index+1,SPAN_EXCLUSIVE_EXCLUSIVE);
                              clean_chain.setSpan(greenBg,0,index+1,SPAN_EXCLUSIVE_EXCLUSIVE);
                              loading_bar.setProgress(index+1,true);

                              reverse_txt.setText(reverse_chain,SPANNABLE);
                              clean_txt.setText(clean_chain,SPANNABLE);

                          }
                          },countr += 150);


                       } else {
                           handler.postDelayed(new Runnable() {
                               @Override
                               public void run() {
                                   reverse_chain.setSpan(redBg, index, index+1, SPAN_EXCLUSIVE_EXCLUSIVE);
                                   clean_chain.setSpan(redBg, index, index + 1, SPAN_EXCLUSIVE_EXCLUSIVE);
                                   reverse_txt.setText(reverse_chain, SPANNABLE);
                                   clean_txt.setText(clean_chain, SPANNABLE);
                               }
                           }, countr +=150);
                           break;
                       }

               }
}

    public void copyAssets(){
        InputStream assetIs = null;
        InputStream assetIs2 = null;
        try {
            assetIs = getApplicationContext().getAssets().open("palindromes.txt");
            assetIs2 = getApplicationContext().getAssets().open("nonpalindromes.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        OutputStream copyOs = null;
        OutputStream copyOs2 = null;
        try {
            copyOs = openFileOutput("palindromes.txt", MODE_PRIVATE);
            copyOs2 = openFileOutput("nonpalindromes.txt", MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[4096];
        int bytesRead;
        byte[] buffer2 = new byte[4096];
        int bytesRead2;
        try {
            while ((bytesRead = assetIs.read(buffer)) != -1) {
                copyOs.write(buffer, 0, bytesRead);
            }
            while ((bytesRead2 = assetIs2.read(buffer2)) != -1) {
                copyOs2.write(buffer2, 0, bytesRead2);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            assetIs.close();
            copyOs.close();
            assetIs2.close();
            copyOs2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    }
