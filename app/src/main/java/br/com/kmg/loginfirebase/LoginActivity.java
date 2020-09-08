package br.com.kmg.loginfirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    TextView tvRegister;
    TextView tvForgetPassword;
    TextInputLayout tilEmail;
    TextInputLayout tilPassword;
    Button btLogin;
    String TAG = "LoginActivity";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        tvRegister = findViewById(R.id.tvAddAccount);
        tvForgetPassword = findViewById(R.id.tvForgotPassword);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        btLogin = findViewById(R.id.btLogin);
        setListeners();
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        startMainMenuScreen(currentUser);
    }

    private void doLogin(String email, String password){
        Toast.makeText(LoginActivity.this, "Iniciando Login.",
                Toast.LENGTH_SHORT).show();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startMainMenuScreen(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            startMainMenuScreen(null);
                        }
                    }
                });
    }

    private void startMainMenuScreen(FirebaseUser firebaseUser){
        if(firebaseUser == null){
            Toast.makeText(LoginActivity.this, "Erro ao recuperar usuário.",Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(LoginActivity.this, "Iniciando Tela principal do app.",Toast.LENGTH_LONG).show();
    }

    private void recoverPassword(String email){
        if(email == null){
            Toast.makeText(LoginActivity.this, "Adicione o email.",Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    };

    private void setListeners(){
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tilEmail.getEditText().getText().toString();
                String password = tilPassword.getEditText().getText().toString();
                doLogin(email, password);
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });
    }
}
