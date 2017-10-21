package com.co2017gmail.bilibili.smarttimer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
/**
 * Created by Yue on 16/10/17.
 */
public class EditActivity extends AppCompatActivity {
    TextView tv_avatar, tv_name, tv_email, tv_phone, tv_pwd;
    EditText et_name, et_email, et_phone;
    ImageView iv_avatar;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        initView();
        initData();
        setListener();
    }


    /**
     * initialize widget
     */
    private void initView() {
        tv_avatar = (TextView) findViewById(R.id.tv_avatar);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_email = (TextView) findViewById(R.id.tv_email);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_pwd = (TextView) findViewById(R.id.tv_pwd);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_phone = (EditText) findViewById(R.id.et_phone);

        tv_avatar.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_name.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_email.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_phone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tv_pwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    /**
     * initialize data
     */
    private void initData() {
//        String username = getIntent().getStringExtra("BiliBili");
        user = UserDB.find(this, "BiliBili");
        et_name.setText(user.userName);
        if (user.email != null) et_email.setText(user.email);
        if (user.phoneNumber != null) et_phone.setText(user.phoneNumber);
        if (user.avator != null) {
            Uri uri = Uri.parse(user.avator);
            iv_avatar.setImageURI(uri);
        }
    }

    /**
     * Listener
     */
    private void setListener() {
        //avatar
        tv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 100);
            }
        });
        //email
        tv_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_email.getText().equals("Edit")){
                    et_email.setEnabled(true);
                    tv_email.setText("Save");
                }else {
                    String email = et_email.getText().toString();
                    user.email = email;
                    UserDB.update(EditActivity.this, user);
                    tv_email.setText("Edit");
                    et_email.setEnabled(false);
                    Toast.makeText(EditActivity.this,"Change email Successed",Toast.LENGTH_LONG).show();
                }
            }
        });
        //phone number
        tv_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(tv_email.getText().equals("Edit")){
                    et_phone.setEnabled(true);
                    et_phone.setText("Save");
                }else {
                    String phone = et_phone.getText().toString();
                    user.phoneNumber = phone;
                    UserDB.update(EditActivity.this, user);
                    et_phone.setText("Edit");
                    et_phone.setEnabled(false);
                    Toast.makeText(EditActivity.this,"Change phoneNumber Successed",Toast.LENGTH_LONG).show();
                }
            }
        });
        //change password
        tv_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChangePwd();
            }
        });
    }

    /**
     * change avatar
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri uri = data.getData();
        if (uri != null) {
            iv_avatar.setImageURI(uri);
            user.avator = uri.toString();
            UserDB.update(this, user);
            Toast.makeText(EditActivity.this,"Change avatar Successed",Toast.LENGTH_LONG).show();
        }
    }

    /**
     * show change password page
     */
    private void showChangePwd(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View view = LayoutInflater.from(this).inflate(R.layout.password_edit,null);
        builder.setView(view);
        builder.setNegativeButton("cancel",null);
        builder.setPositiveButton("confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                EditText original = (EditText) view.findViewById(R.id.original);
                EditText pwd1 = (EditText) view.findViewById(R.id.pwd1);
                EditText pwd2 = (EditText) view.findViewById(R.id.pwd2);
                //original password is wrong
                if(user.password!=null&&!original.getText().toString().equals(user.password)){
                    Toast.makeText(EditActivity.this,"Faild, The original password is not correct",Toast.LENGTH_LONG).show();
                    return;
                }
                //entered two new passwords are not consistent
                if(!pwd1.getText().toString().equals(pwd2.getText().toString())){
                    Toast.makeText(EditActivity.this,"Faild, The password you entered two times is not consistent",Toast.LENGTH_LONG).show();
                    return;
                }
                user.password = pwd1.getText().toString();
                UserDB.update(EditActivity.this, user);
                Toast.makeText(EditActivity.this,"Change password Successed",Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }
}
