package com.example.memehouse;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.*;
import android.util.Log;
import android.content.Context;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.VM.VModel;
import com.example.RetrofitApiCaller.apicall;
import com.example.RetrofitApiCaller.RetroFetch;
import com.example.RetrofitApiCaller.Memes;
import com.example.RetrofitApiCaller.MemesFetch;
import com.example.memehouse.databinding.ActivityMainBinding;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.internal.GoogleServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.itsaky.androidide.logsender.LogSender;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
public class MainActivity extends AppCompatActivity {
    private SwipeRefreshLayout layout;
    private ActivityMainBinding binding;
    private String MemeApi = "https://meme-api.com"; // gimme
    private ArrayList<Memes> memes = new ArrayList<>();
    private VModel ViewModel;
    private GoogleSignInAccount account;
    public DrawerLayout db;
    public int Quantity=10;
    private FloatingActionButton button;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Remove this line if you don't want AndroidIDE to show this app's logs
        LogSender.startLogging(this);
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        
        setContentView(binding.getRoot());
        ViewModel = new ViewModelProvider(this).get(VModel.class);
        Alert();
        layout = binding.Swipe;
        response();
        layout.setOnRefreshListener(
                () -> {
                    response();
                });

        account = GoogleSignIn.getLastSignedInAccount(this);
        SetupNavs(account);
        ViewModel.Memes.observe(
                this,
                (Obj) -> {
                    if (Obj != null) {}
                    Setup(Obj);
                });
        button = binding.Quantity;
        button.setOnClickListener(V->{
            MaterialTextDialog();
        }); 
    }

    void signOut() {
        GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

        GoogleSignInClient client = GoogleSignIn.getClient(this, gso);
        client.signOut()
                .addOnCompleteListener(
                        new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(Task<Void> arg0) {
                                finish();
                                startActivity(new Intent(MainActivity.this, GLogin.class));
                            }
                        });
    }

    void Setup(List<Memes> m) {

        RecyclerView v = binding.Re;
        v.setLayoutManager(new LinearLayoutManager(this));
        MemeAdapter ml = new MemeAdapter(m, MainActivity.this);
        v.setAdapter(ml);
        ml.notifyDataSetChanged();
        layout.setRefreshing(false);
    }

    void response() {
        layout.setRefreshing(true);
        RetroFetch.getInstance(MemeApi)
                .Api
                .getMemes(Quantity)
                .enqueue(
                        new Callback<MemesFetch>() {
                            @Override
                            public void onResponse(
                                    Call<MemesFetch> arg0, Response<MemesFetch> arg1) {

                                MemesFetch f = arg1.body();
                                if (f != null) {
                                    ViewModel.setMemes(f.getMemes());
                                    // Toast.makeText(getApplicationContext(),f.getMemes().get(0).getUrl()+f.getMemes().get(1).getUrl(),0).show();
                                } else {
                                    response();
                                }
                            }

                            @Override
                            public void onFailure(Call<MemesFetch> arg0, Throwable arg1) {
                                Toast.makeText(
                                                getApplicationContext(),
                                                arg1.getMessage().toString(),
                                                0)
                                        .show();
                            }
                        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu arg0) {
        // TODO: Implement this method
        new MenuInflater(this).inflate(R.menu.menusmain, arg0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem arg0) {
        // TODO: Implement this method
        switch (arg0.getItemId()) {
            case R.id.LOG_Out:
                signOut();
                break;
        }
        return super.onOptionsItemSelected(arg0);
    }

    void SetupNavs(GoogleSignInAccount acc) {

        Toolbar tb = binding.Toolbar;
         db = binding.Drawer;
        NavigationView nv = binding.Navigation;
        ActionBarDrawerToggle toogle;
        setSupportActionBar(tb);

        toogle = new ActionBarDrawerToggle(this, db, tb, R.string.N_Open, R.string.N_close);
        db.addDrawerListener(toogle);
        toogle.syncState();

        View v = nv.getHeaderView(0);
        ImageView img = v.findViewById(R.id.headerImage);
        TextView text = v.findViewById(R.id.headerName);
        MaterialShapeDrawable dr=(MaterialShapeDrawable) nv.getBackground();
        dr.setShapeAppearanceModel(dr.getShapeAppearanceModel().toBuilder().setTopRightCorner(CornerFamily.ROUNDED,32).build());
        Glide.with(this).load(acc.getPhotoUrl()).circleCrop().into(img);
        text.setText(acc.getDisplayName());
        nv.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {

                    @Override
                    public boolean onNavigationItemSelected(MenuItem arg0) {
                         switch(arg0.getItemId()){
                             case R.id.menu_item1:
                             Showsheet();
                             db.closeDrawer(GravityCompat.START);
                             break;
                         }
                      return true;
                    }
                });
    }

    void Alert() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this)
                        .setTitle("Alert")
                        .setMessage("Thanks For trying. if you are Facing Same Image Error (Its an Api Bug) you can refresh the ui by swiping from up")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                
                            }
                        });
                builder.create();
                builder.show();
    }

    void Showsheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(MainActivity.this, R.style.BottomSheet);
        View v = getLayoutInflater().inflate(R.layout.bottomsheetlayout, null);
        ImageView git,insta,Linked;
        git= v.findViewById(R.id.GIT);
        insta =v.findViewById(R.id.Insta);
        Linked=v.findViewById(R.id.Linked);
        insta.setOnClickListener(V->{
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://instagram.com/___ayan____malik?igshid=NTc4MTIwNjQ2YQ==")));
        });
        git.setOnClickListener(L->{
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://github.com/mohammad-ayan-008/")));
        });
        Linked.setOnClickListener(J->{
            startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.linkedin.com/mwlite/in/mohammad-ayan-5921a7250")));
        });
        dialog.setContentView(v);
        dialog.show();
    }
    
    
    void MaterialTextDialog(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainActivity.this);
        builder.setTitle("Set Quantity");
        View v=getLayoutInflater().inflate(R.layout.textlayout,null);
        builder.setView(v);
        TextInputEditText edit = v.findViewById(R.id.edit_text);
        edit.requestFocus();
        builder.setPositiveButton("Done",(DialogInterface i,int j)->{
            i.cancel();
               
            int temp= Integer.parseInt(edit.getText().toString());
            if(temp>0){
                Quantity=temp;
            }
                   
        });
     
        builder.setNegativeButton("Cancel",(DialogInterface i,int j)->{
            i.cancel();
        });
        builder.create().show();
        
        
    }
}
