package com.example.memehouse;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.example.RetrofitApiCaller.Memes;
import com.example.RetrofitApiCaller.apicall;
import com.example.memehouse.databinding.MemeslayoutBinding;
import java.util.ArrayList;
import java.util.List;

public class MemeAdapter extends RecyclerView.Adapter<MemeAdapter.Holder> {
    private List<Memes> list;
    private Context c;

    public MemeAdapter(List<Memes> list, Context c) {
        this.list = list;
        this.c = c;
    }

    class Holder extends RecyclerView.ViewHolder
            implements View.OnLongClickListener, PopupMenu.OnMenuItemClickListener {
        MemeslayoutBinding binding;

        Holder(MemeslayoutBinding bind) {
            super(bind.getRoot());
            binding = bind;
            binding.coordinator.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View arg0) {
            menu(arg0);
            return true;
        }

        void menu(View w) {
            PopupMenu m = new PopupMenu(w.getContext(), w);
            m.inflate(R.menu.operations);
            m.show();
            m.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem arg0) {
            switch(arg0.getItemId()){
                case R.id.menu_i1:
                Download(list.get(getAdapterPosition()).getUrl());
                break;
                case R.id.menu_i2:
                Share(list.get(getAdapterPosition()).getUrl());
                break;
            }
            
            
            return false;
        }
        
        public void Download(String Url){
            int index_last = Url.lastIndexOf(".");
            DownloadManager.Request req = new DownloadManager.Request(Uri.parse(Url));
            req.setDescription("Downloading....");
            req.setTitle("Wait For a While");
            req.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            req.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,Calendar.getInstance().getTime().toString()+Url.substring(index_last));
            DownloadManager m = (DownloadManager) c.getSystemService(Context.DOWNLOAD_SERVICE);
            m.enqueue(req);
        }
        public void Share(String url){
            Intent i= new Intent(Intent.ACTION_SEND);
            i.setType("text/*");
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra(Intent.EXTRA_TEXT,url);
            c.startActivity(Intent.createChooser(i,null));
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup arg0, int arg1) {
        return new MemeAdapter.Holder(MemeslayoutBinding.inflate(LayoutInflater.from(c)));
    }

    @Override
    public void onBindViewHolder(Holder arg0, int arg1) {
        Glide.with(c).load(list.get(arg1).getUrl()).into(arg0.binding.Image);
        arg0.binding.Title.setText("Title :- " + list.get(arg1).getTitle() + "\n\n ");
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
