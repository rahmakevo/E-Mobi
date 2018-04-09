package ke.co.dev.siundu344.emobi.ui;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import ke.co.dev.siundu344.emobi.R;

public class BalanceActivity extends AppCompatActivity {
    private DatabaseReference mBalRef;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

        Toolbar mBalBar = findViewById(R.id.toolBarBalance);
        setSupportActionBar(mBalBar);
        getSupportActionBar().setTitle("Account Balance");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final TextView mBal = findViewById(R.id.textBal);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        final String mUid = mUser.getUid();
        mBalRef = FirebaseDatabase.getInstance().getReference();
        mBalRef.child(mUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String bal = dataSnapshot.child("balance").getValue().toString();
                        mBal.setText(bal);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        final EditText mEditBal = findViewById(R.id.editTextBal);

        Button btnCompute = findViewById(R.id.btnCompute);
        btnCompute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String bal = mEditBal.getText().toString();
                if (!TextUtils.isEmpty(bal)) {
                    mBalRef.child(mUid).child("balance").setValue(bal)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(BalanceActivity.this, "Your Account Balance has been updated", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    Toast.makeText(BalanceActivity.this, "A new Balance Should be recorded", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
