package com.hooooong.pholar.view.sign;

import android.content.Intent;
<<<<<<< HEAD
=======
import android.content.SharedPreferences;
>>>>>>> 87ccb01a15d14705ce56208c28883172229d5986
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.hooooong.pholar.R;
import com.hooooong.pholar.model.User;
import com.hooooong.pholar.view.home.HomeActivity;

public class SigninActivity extends AppCompatActivity {
    // private Button btnLoginFacebook;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseUser fUser;
    public final int RC_SIGN_IN = 12;
    
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        // initView();

        progressBar = findViewById(R.id.progressBar);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        mGoogleApiClient.stopAutoManage(SigninActivity.this);
                        mGoogleApiClient.disconnect();
                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("user");
        SignInButton button = findViewById(R.id.btnLoginGoogle);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        mGoogleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(@Nullable Bundle bundle) {
                if (mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                }
            }

            @Override
            public void onConnectionSuspended(int i) {
            }
        });

        super.onStart();
    }
    String email, nickname, photo_path;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressBar.setVisibility(View.VISIBLE);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {

                // Google Sign In was successful, authenticate with Firebase
                //Toast.makeText(SigninActivity.this, "아이디 생성이 완료 되었습니다", Toast.LENGTH_SHORT).show();
                GoogleSignInAccount account = result.getSignInAccount();
                photo_path = account.getPhotoUrl().toString();
                email = account.getEmail();
                nickname = account.getDisplayName();

                firebaseAuthWithGoogle(account);
//                editor.putString("email", account.getEmail());
//                editor.commit();
            } else {
                // Google Sign In failed, update UI appropriately
                // ...
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(SigninActivity.this, "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SigninActivity.this, "로그인이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            fUser = mAuth.getCurrentUser();

                            User user = new User();
                            user.user_id = fUser.getUid();
                            user.token = FirebaseInstanceId.getInstance().getToken();
                            user.nickname = nickname;
                            user.email = email;
                            user.profile_path = photo_path;

                            checkUser(user);
<<<<<<< HEAD
=======

>>>>>>> 87ccb01a15d14705ce56208c28883172229d5986
                            // ----- For Test -----
//                            Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
//                            SigninActivity.this.startActivity(intent);
//                            finish();
                            // --------------------

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ProgressBar progressBar = findViewById(R.id.progressBar);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
    }

    DatabaseReference userRef;
<<<<<<< HEAD
=======

>>>>>>> 87ccb01a15d14705ce56208c28883172229d5986
    private void checkUser(final User user) {
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user");
        userRef.child(user.user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    userRef.child(user.user_id).setValue(user);
                    Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SigninActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                finish();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

