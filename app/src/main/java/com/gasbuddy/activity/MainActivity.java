package com.gasbuddy.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gasbuddy.R;
import com.gasbuddy.databinding.MainActivityBinding;
import com.gasbuddy.imageloading.ImageLoader;
import com.gasbuddy.models.PhotoDetails;
import com.gasbuddy.viewmodel.ImagesViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public MainActivityBinding mainActivityBinding;
    private ImagesViewModel imagesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivityBinding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        mainActivityBinding.setLifecycleOwner(this);
        initComponents(savedInstanceState);
    }

    private void initComponents(Bundle savedInstanceState) {
        imagesViewModel = new ViewModelProvider(this).get(ImagesViewModel.class);
        if (savedInstanceState == null) {
            imagesViewModel.init(new ImageLoader(getApplicationContext()));
        }
        mainActivityBinding.setModel(imagesViewModel);
        setListeners();
    }

    private void setListeners() {
        mainActivityBinding.edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    imagesViewModel.performSearch(v.getText().toString().trim().toLowerCase());
                    return true;
                }
                return false;
            }
        });
        imagesViewModel.getUseCache().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                imagesViewModel.setPhotosListInAdapter(null);
            }
        });
        imagesViewModel.getNetworkMethod().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                imagesViewModel.fetchList();
            }
        });
        imagesViewModel.getSource().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                imagesViewModel.fetchList();
            }
        });
        imagesViewModel.getMode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (!(mainActivityBinding.recyclerView.getLayoutManager() instanceof GridLayoutManager)) {
                    mainActivityBinding.imgMode.setSelected(true);
                    mainActivityBinding.recyclerView.setLayoutManager(new GridLayoutManager(MainActivity.this, 3));
                } else {
                    mainActivityBinding.imgMode.setSelected(false);
                    mainActivityBinding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                }
                imagesViewModel.getAdapter().notifyDataSetChanged();
            }
        });
        imagesViewModel.getPhotosList().observe(this, new Observer<List<PhotoDetails>>() {
            @Override
            public void onChanged(List<PhotoDetails> photoDetailsList) {
                imagesViewModel.loading.set(View.GONE);
                if (photoDetailsList.size() == 0) {
                    imagesViewModel.showEmpty.set(View.VISIBLE);
                } else {
                    imagesViewModel.showEmpty.set(View.GONE);
                    imagesViewModel.preserveOriginalData(photoDetailsList);
                    imagesViewModel.setPhotosListInAdapter(photoDetailsList);
                }
            }
        });
        imagesViewModel.getSelected().observe(this, new Observer<PhotoDetails>() {
            @Override
            public void onChanged(PhotoDetails photoDetails) {
                Intent intent = new Intent(MainActivity.this, ImageDetailsActivity.class);
                if (photoDetails.urls != null) {
                    intent.putExtra("PHOTO_DETAILS", photoDetails);
                    intent.putExtra("IS_GOOGLE", false);
                } else {
                    intent.putExtra("PHOTO_DETAILS", photoDetails);
                    intent.putExtra("IS_GOOGLE", true);
                }
                startActivity(intent);
            }
        });
        imagesViewModel.loading.set(View.VISIBLE);
        imagesViewModel.fetchList();
    }


}