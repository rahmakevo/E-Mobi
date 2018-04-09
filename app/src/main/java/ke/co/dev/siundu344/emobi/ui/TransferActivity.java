package ke.co.dev.siundu344.emobi.ui;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import ke.co.dev.siundu344.emobi.R;

public class TransferActivity extends AppCompatActivity {
    private EditText mAmount, mAccNo;
    private ProgressDialog mSendDialog;
    private DatabaseReference mTransferRef, mAccountRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        // ToolBar
        Toolbar mTransferBar = findViewById(R.id.toolBarTransfer);
        setSupportActionBar(mTransferBar);
        getSupportActionBar().setTitle("Transfer Funds");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mSendDialog = new ProgressDialog(this);
        mTransferRef = FirebaseDatabase.getInstance().getReference();
        mAccountRef = FirebaseDatabase.getInstance().getReference();
        mAmount = findViewById(R.id.editTextAmount);
        mAccNo = findViewById(R.id.editTextReceive);

        Button btnSend = findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = mAmount.getText().toString();
                String accNo = mAccNo.getText().toString();

                if (!TextUtils.isEmpty(amount) && !TextUtils.isEmpty(accNo)) {
                    mSendDialog.setTitle("Transferring Funds");
                    mSendDialog.setMessage("Please wait while we process your request");
                    mSendDialog.setCanceledOnTouchOutside(false);
                    mSendDialog.show();

                    transfer_funds(amount, accNo);

                } else {
                    Toast.makeText(TransferActivity.this, "All input fields must be correctly filled", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void transfer_funds(final String amount, String accNo) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        final String mUid = mUser.getUid();
        HashMap<String, String> mTransferMap = new HashMap<>();
        mTransferMap.put("to", accNo);
        mTransferMap.put("amount", amount);
        mTransferRef.child(mUid).child("transfers").push().setValue(mTransferMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mAccountRef.child(mUid)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String bal = dataSnapshot.child("balance").getValue().toString();

                                            int previousBal = Integer.parseInt(bal);
                                            int amountTransfer = Integer.parseInt(amount);

                                            int newBal = previousBal - amountTransfer;
                                            String finalBal = String.valueOf(newBal);
                                            mAccountRef.child(mUid).child("balance").setValue(finalBal)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                mSendDialog.dismiss();
                                                                Toast.makeText(TransferActivity.this, "Transfer made successfully", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(TransferActivity.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                        } else {
                            Toast.makeText(TransferActivity.this, "Check your internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
