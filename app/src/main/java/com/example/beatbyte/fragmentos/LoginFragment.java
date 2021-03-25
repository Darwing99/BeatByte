package com.example.beatbyte.fragmentos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.beatbyte.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;


public class LoginFragment extends Fragment {
    private GoogleSignInClient googleSignInClient;
    private static final int SIGN_IN_CODE=666;
    private FirebaseAuth firebaseAuth;
    View vista;
    TextView nombre,descripcion;
    ImageView perfil;
    SignInButton btnLogin;
    Button cerrar,iniciar;
    Fragment fragmento;
    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vista=inflater.inflate(R.layout.fragment_login, container, false);


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient= GoogleSignIn.getClient(vista.getContext(),gso);
        firebaseAuth=FirebaseAuth.getInstance();
        nombre=vista.findViewById(R.id.namegoogle);
        descripcion=vista.findViewById(R.id.descripcion);
        perfil=vista.findViewById(R.id.perfil);
        cerrar=vista.findViewById(R.id.cerrarsesion);
        btnLogin=vista.findViewById(R.id.loginGoogle);
        btnLogin.setSize(SignInButton.SIZE_STANDARD);

        btnLogin.setOnClickListener(login->{

            googleLogin();

        });
        cerrar.setOnClickListener(cerrar->{
            firebaseAuth.signOut();

        });

        return vista;
    }

    private void googleLogin(){
        Intent intent=googleSignInClient.getSignInIntent();
        startActivityForResult(intent,SIGN_IN_CODE);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==SIGN_IN_CODE){
            Task<GoogleSignInAccount> tarea = GoogleSignIn.getSignedInAccountFromIntent(data);
            manejadorLoginGoogle(tarea);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



    private void manejadorLoginGoogle(Task<GoogleSignInAccount> tarea) {
        try {
            GoogleSignInAccount account = tarea.getResult(ApiException.class);

//            updateUI(account);
             LoginFirebase(account.getIdToken());

        } catch (ApiException e) {

            updateUI(null);
        }

    }

    private void LoginFirebase(String idToken){
        AuthCredential authCredential= GoogleAuthProvider.getCredential(idToken,null);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener((Activity) vista.getContext(), (OnCompleteListener<AuthResult>) task -> {
                    if(task.isSuccessful()){

                        updateUI( firebaseAuth.getCurrentUser());
                    }else{
                        updateUI(null);
                        Toast.makeText(vista.getContext(),"Acceso denegado",Toast.LENGTH_LONG).show();
                    }

                });


    }

    @Override
    public void onStart() {
        super.onStart();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(vista.getContext());
        FirebaseUser user=firebaseAuth.getCurrentUser();
        updateUI(user);


        if(user!=null){
            account.getFamilyName();
//            Glide.with(vista.getContext())
//                    .load(account.getPhotoUrl()).centerCrop()
//                    .circleCrop()
//                    .into(perfil);
            Picasso.get()
                    .load(account.getPhotoUrl())
                    .placeholder(R.mipmap.loader)
                    .fit()
                    .centerCrop()
                    .into(perfil, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "onSuccess: Correcto");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.d(TAG, "onSuccess: Algo salio mal :c", e);
                        }
                    });


            perfil.setVisibility(View.VISIBLE);
            nombre.setText(account.getFamilyName());
            nombre.setGravity(View.TEXT_ALIGNMENT_CENTER);
            btnLogin.setVisibility(View.GONE);

            cerrar.setVisibility(View.VISIBLE);
            descripcion.setVisibility(View.GONE);

        }else{
            descripcion.setVisibility(View.VISIBLE);

        }
    }
    private  void updateUI(FirebaseUser account){

    }

//    private void updateUI(GoogleSignInAccount account){
//        if(account!=null){
//            Intent intent=new Intent(vista.getContext(), MainActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//
//        }
//
//
//    }
}