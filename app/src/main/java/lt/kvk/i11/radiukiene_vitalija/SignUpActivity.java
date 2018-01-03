package lt.kvk.i11.radiukiene_vitalija;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    EditText editTextEmail, editTextPassword;       // deklaruojami GUI vaizdiniai objektai
    ProgressBar progressBar;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);            // inicializuojamis views is xml failo
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        progressBar = (ProgressBar) findViewById(R.id.progressbar);

        findViewById(R.id.buttonSignUp).setOnClickListener(this);       // pridedamas listener mygtukui
        findViewById(R.id.textViewLogin).setOnClickListener(this);      // pridedamas listener tekstui

        mAuth = FirebaseAuth.getInstance();     // inicializacija
    }

    private void registerUser(){            // metodas naudotojo registravimui
        String email = editTextEmail.getText().toString().trim();           // gaunamos ivestos reiksmes
        String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {              // jei email laukelis tuscias
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;         // toliau nevykdoma
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {     // jei email neatitinka
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;         // toliau nevykdoma
        }

        if (password.isEmpty()) {           // jei password laukelis tuscias
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;         // toliau nevykdoma
        }

        if (password.length()<6) {          // jei password trumpesnis nei 6 simboliai
            editTextPassword.setError("Minimum lenght of password should be 6");
            editTextPassword.requestFocus();
            return;     // toliau nevykdoma
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {        // atliekama naudotojo registracija su email ir password
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {      // jei registracija pavyko
                    finish();
                    startActivity(new Intent(SignUpActivity.this, ProfileActivity.class));  // atidaromas ProfileActivity langas
                } else {
                    //Toast.makeText(getApplicationContext(), "Some Error Occurred", Toast.LENGTH_SHORT).show();
                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSignUp:         // jei paspaudziamas mygtukas Sign up

                registerUser();
                break;

            case R.id.textViewLogin:        // jei paspaudziamas tekstas
                finish();
                startActivity(new Intent(this, MainActivity.class));    // atidaromas MainActivity langas
                break;
        }

    }
}
