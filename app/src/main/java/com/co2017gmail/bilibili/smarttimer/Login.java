package com.co2017gmail.bilibili.smarttimer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



/**
 * Created by jkjohnwuhan on 10/10/2017.
 */

public class Login extends AppCompatActivity implements View.OnClickListener{

//    private DbHelper db;
//    private Session session;
    private Button login, register;
    private EditText etEmail, etPass;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        db = new DbHelper(this);
//        session = new Session(this);
        login = (Button)findViewById(R.id.btnLogin);
        register = (Button)findViewById(R.id.btnReg);
        etEmail = (EditText)findViewById(R.id.etEmail);
        etPass = (EditText)findViewById(R.id.etPass);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

//    if(session.loggedin()){
//        startActivity(new Intent(LoginActivity.this,MainActivity.class));
//        finish();
//    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btnLogin:
                login();
                break;
            case R.id.btnReg:
                startActivity(new Intent(Login.this,Register.class));
                break;
            default:

        }
    }

    private void login() {
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();

//        if(db.getUser(email,pass)){
//            session.setLoggedin(true);
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        }else{
//            Toast.makeText(getApplicationContext(), "Wrong email/password",Toast.LENGTH_SHORT).show();
//        }
    }

}
