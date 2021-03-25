package com.example.beatbyte;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;

import com.example.beatbyte.fragmentos.BuscarFragment;
import com.example.beatbyte.fragmentos.InicioFragment;
import com.example.beatbyte.fragmentos.LoginFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private GoogleSignInClient googleSignInClient;
    Fragment fragment;

    private FirebaseAuth firebaseAuth;
    FragmentManager manager=getSupportFragmentManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth=FirebaseAuth.getInstance();
        fragment=new InicioFragment();
        manager.beginTransaction().replace(R.id.main,fragment).commit();


        BottomNavigationView menu=findViewById(R.id.menu);
//      Fragmentos de vista principal
        menu.setOnNavigationItemSelectedListener(item->{

            switch (item.getItemId()){
                case R.id.home:
                    fragment=new InicioFragment();

                    break;
                case R.id.listas:
                    fragment=new ListFragment();

                    break;
                case R.id.biblioteca:

                    break;
                case R.id.buscar:
                    fragment=new BuscarFragment();
                    break;



            }
               manager.beginTransaction().replace(R.id.main,fragment).setCustomAnimations(R.animator.style,R.animator.style).commit();
            return true;
        });


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleSignInClient= GoogleSignIn.getClient(this,gso);


    }

    @Override
    protected void onStart() {
        super.onStart();

       // GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        updateUI(firebaseAuth.getCurrentUser());
   }


    private void updateUI(FirebaseUser user){
//        if(user==null){
//            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//
//        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menusuperior, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId()==R.id.configuracion){
            fragment=new LoginFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main,fragment).commit();


        }
        return super.onOptionsItemSelected(item);
    }

}