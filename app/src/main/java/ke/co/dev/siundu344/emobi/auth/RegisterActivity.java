package ke.co.dev.siundu344.emobi.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import ke.co.dev.siundu344.emobi.R;
import ke.co.dev.siundu344.emobi.ui.MainActivity;

public class RegisterActivity extends AppCompatActivity {
    private ProgressDialog mRegDialog;
    private EditText mNames, mEmail, mPass, mPhone, mIdPass, mAccNo;
    private FirebaseAuth mAuth;
    private DatabaseReference mUserRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        
        // Register ToolBar
        Toolbar mRegBar = findViewById(R.id.toolBarReg);
        setSupportActionBar(mRegBar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        
        mRegDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference();
        
        // find all editText By Id
        mNames = findViewById(R.id.editTextRegNames);
        mEmail = findViewById(R.id.editTextRegEmail);
        mPass = findViewById(R.id.editTextRegPass);
        mPhone = findViewById(R.id.editTextRegNumber);
        mIdPass = findViewById(R.id.editTextRegId);
        mAccNo = findViewById(R.id.editTextRegAccNo);

        Button btnReg = findViewById(R.id.btnRegister);
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String names = mNames.getText().toString();
                String email = mEmail.getText().toString();
                String pass = mPass.getText().toString();
                String phone = mPhone.getText().toString();
                String idPass = mIdPass.getText().toString();
                String accNo = mAccNo.getText().toString();
                // check if values are correctly inserted
                if ((!TextUtils.isEmpty(names)) && (!TextUtils.isEmpty(email)) && (pass.length() >= 6) && (!TextUtils.isEmpty(phone)) && (!TextUtils.isEmpty(idPass)) && (!TextUtils.isEmpty(accNo))) {
                    // start a progress dialog
                    mRegDialog.setTitle("Creating Account");
                    mRegDialog.setMessage("Please wait while we prepare an account for you");
                    mRegDialog.setCanceledOnTouchOutside(false);
                    mRegDialog.show();
                    
                    createClient(names, email, pass, phone, idPass, accNo);
                } else {
                    Toast.makeText(RegisterActivity.this, "All input fields must be correctly inserted", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createClient(final String names, String email, String pass, final String phone, final String idPass, final String accNo) {
        // create user account with email and password
        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // check if task is successful
                        if (task.isSuccessful()) {
                            // get the id of the created user
                            FirebaseUser mUser = mAuth.getCurrentUser();
                            String mUid = mUser.getUid();
                            // store user data in its own child
                            String mDate = DateFormat.getDateTimeInstance().format(new Date);
                            HashMap<String, String> mUserMap = new HashMap<>();
                            mUserMap.put("name", names);
                            mUserMap.put("phone", phone);
                            mUserMap.put("id", idPass);
                            mUserMap.put("acc", accNo);
                            mUserMap.put("date", mDate);
                            mUserRef.child(mUid).setValue(mUserMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            // check if task is successful
                                            if (task.isSuccessful()) {
                                                Intent mRegIntent = new Intent(RegisterActivity.this, MainActivity.class);
                                                startActivity(mRegIntent);
                                                finish();
                                            } else {
                                                Toast.makeText(RegisterActivity.this, "Please check your Internet Connectivity", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Please check your Internet Connectivity", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
