package com.example.memehouse;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.memehouse.databinding.LayoutGLoginBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GLogin extends AppCompatActivity implements View.OnClickListener{
 private LayoutGLoginBinding binding;   
 private GoogleSignInClient client;   
    @Override
        public void onCreate(Bundle arg0) {
            super.onCreate(arg0);
            // TODO: Implement this method
            //setTheme(R.style.Theme_Application);
            binding= LayoutGLoginBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());
           
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build();  
        
        
            client = GoogleSignIn.getClient(this, gso);
        
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            if(account!=null){
                 Toast.makeText(this,"Logined as:-"+account.getEmail(),0).show();
                 startActivity(new Intent(this,MainActivity.class));
                 finish();
            }
        
        
            SignInButton signInButton = binding.sign; 
            signInButton.setSize(SignInButton.SIZE_WIDE);
            signInButton.setOnClickListener(this);
            Animation animation = AnimationUtils.loadAnimation(this,R.anim.fade);
            animation.setInterpolator(new OvershootInterpolator());
            LinearLayout LL = binding.LL;
            animation.setRepeatCount(0);
            LL.startAnimation(animation);
            
        }
    
      @Override
      public void onClick(View v){
        Intent signInIntent = client.getSignInIntent();
        startActivityForResult(signInIntent, 100);
      }
    
    
      @Override
      protected void onActivityResult(int arg0, int arg1, Intent arg2) {
         if(arg0==100){
             Task<GoogleSignInAccount>  login = GoogleSignIn.getSignedInAccountFromIntent(arg2);
             handle(login);
         }
     }
        
    
    
     void handle(Task<GoogleSignInAccount> acc){
         try{
             GoogleSignInAccount account = acc.getResult(ApiException.class);
            Toast.makeText(this,"Logined as:-"+account.getEmail().toString(),0).show();
            startActivity(new Intent(this,MainActivity.class));
            finish();
         }catch(Exception e){
             
         }
     }
}
    
        

