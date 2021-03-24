package com.example.beatbyte.fragmentos;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.beatbyte.MainActivity;
import com.example.beatbyte.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistroFragment extends Fragment {
    private com.google.android.material.textfield.TextInputEditText nombre;
    private com.google.android.material.textfield.TextInputEditText apellido;
    private com.google.android.material.textfield.TextInputEditText email;
    private com.google.android.material.textfield.TextInputEditText password;

    /////GOOGLE y FIREBASE/////
    private GoogleSignInClient googleSignInClient;
    private final int SIGN_IN_CODE=777;
    View vista;
    private Button guardar;
    private SignInButton google;
    private String name;
    private String apell;
    private String correo;
    private String pass;
    FirebaseApp app;
    FirebaseAuth firebaseAuth;
    DatabaseReference dbreferencia;

    public RegistroFragment() {
        // Required empty public constructor
    }
     Fragment fragmento=new Fragment();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ///////////////////////////////////////////////////////////////
        //////////////////////////////////////////////////////////////
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.


        vista=inflater.inflate(R.layout.fragment_registro, container, false);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()  // peticion a email para autenticacion
                .build();

        googleSignInClient= GoogleSignIn.getClient(vista.getContext(),gso);

        nombre=vista.findViewById(R.id.nombre);
        apellido=vista.findViewById(R.id.apellido);
        email=vista.findViewById(R.id.email);
        password=vista.findViewById(R.id.password);
        guardar=vista.findViewById(R.id.btnCrear);


        google=vista.findViewById(R.id.btnRegistro);
        google.setOnClickListener(registrar->{

            googleRegistro();

        });


        app = FirebaseApp.initializeApp(vista.getContext());
        firebaseAuth=FirebaseAuth.getInstance();
        dbreferencia= FirebaseDatabase.getInstance().getReference();

        Button login=vista.findViewById(R.id.Log_in);



        guardar.setOnClickListener(click->{
            name=nombre.getText().toString();

            apell=apellido.getText().toString();
            correo=email.getText().toString();
            pass=password.getText().toString();

            if(!name.isEmpty() && !apell.isEmpty() && !correo.isEmpty() && !pass.isEmpty()){


                Log.i("correcto",correo);
                Log.i("correcto",name);
                Log.i("correcto",apell);

                   crearRegistro();

            }else{
                Toast.makeText(this.getActivity(), "Los campos estan vacios", Toast.LENGTH_SHORT).show();
            }


        });

        return vista;
    }




    public void crearRegistro(){
        firebaseAuth.createUserWithEmailAndPassword(correo,pass).addOnCompleteListener(task -> {
            if(task.isSuccessful()){

                Map<String,Object> lista=new HashMap<>();
                lista.put("nombre",name);
                lista.put("apellido",apell);
                lista.put("email",correo);
                lista.put("password",pass);

                String id=firebaseAuth.getCurrentUser().getUid();
                    dbreferencia.child("Users").child(id).setValue(lista).addOnCompleteListener(datos -> {
                        if(datos.isSuccessful()){
                            Log.i("conexion","enviar");
                            Intent intent=new Intent(this.getContext(), MainActivity.class);
                            startActivity(intent);

                        }else{
                            Log.i("conexion","no se pudo enviar");
                        }

                    });
            }else{
                Toast.makeText(this.getActivity(),"No se pudo guardar",Toast.LENGTH_LONG).show();
            }

        });

    }




    //Autenticacion con google

    private void googleRegistro(){
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

            updateUI(account);
        } catch (ApiException e) {

            updateUI(null);

        }

    }

    private void updateUI(GoogleSignInAccount account){
        if(account!=null){
            Intent intent=new Intent(vista.getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

    }
}