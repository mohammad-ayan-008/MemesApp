package com.example.VM;

import android.app.Application;
import android.content.Context;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.RetrofitApiCaller.Memes;
import java.util.List;

public class VModel extends AndroidViewModel {

    public VModel(Application c) {
        super(c);
    }

    public MutableLiveData<List<Memes>> Memes=new MutableLiveData<>();


    public void setMemes(List<Memes> Meme) {
        Memes.setValue(Meme);
    }
}
