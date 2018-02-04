package com.example.thom.projetcaron;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {


    private Button btn_connect;
    private EditText mPassword;
    private Button quit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mPassword = (EditText) findViewById(R.id.password);
        btn_connect = (Button) findViewById(R.id.btn_connect);
        quit = (Button) findViewById(R.id.btn_retour);
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_connect.setOnClickListener(new OnClickListener() {
            Intent intent = new Intent(LoginActivity.this,AdminActivity.class);
            @Override
            public void onClick(View view) {
              if (mPassword.getText().toString().isEmpty()){
                  Toast.makeText(getApplicationContext(), getResources().getString(R.string.empty_password),Toast.LENGTH_SHORT).show();
              }
              else{
                  if ( mPassword.getText().toString().equals("perec")) startActivity(intent);
                  else Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrong_password),Toast.LENGTH_SHORT).show();
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
                final Dialog dialog = new Dialog(LoginActivity.this);
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
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                break;

            case R.id.admin:
                break;
        }
        return true;
    }
}

