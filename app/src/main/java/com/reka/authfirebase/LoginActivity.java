package com.reka.authfirebase;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    Boolean doubleBackpress = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    }



    @Override
    protected void onStart() {
        super.onStart();
        //ngecek ada user yang udah login apa blom
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() !=null){
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);
            finish();
        }else {
            startActivityForResult(
                    AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(
                            Arrays.asList(
                                    //untuk nomer hp
                                    new  AuthUI.IdpConfig.Builder(AuthUI.PHONE_VERIFICATION_PROVIDER).build(),
                                    //untuk email google
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build())
                    ).build(),RC_SIGN_IN
            );
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
            //jika sukses masuk
            if (resultCode == RESULT_OK){
                Intent sukses = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(sukses);
            }else {
                //gagal masuk
                if (idpResponse == null){
                    Toast.makeText(this, "Gagal Login", Toast.LENGTH_SHORT).show();
                return;
                }
                if (idpResponse.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
                    Toast.makeText(this, "TIdak Ada Koneksi Internet", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (idpResponse.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR){
                    Toast.makeText(this, "Error 2", Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(this, "Error 1", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
