package com.gasbuddy.activity;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.gasbuddy.R;
import com.gasbuddy.databinding.ActivityImageDetailsBinding;
import com.gasbuddy.imageloading.AppUtils;
import com.gasbuddy.imageloading.ImageLoader;
import com.gasbuddy.models.PhotoDetails;
import com.gasbuddy.viewmodel.ImagesViewModel;

public class ImageDetailsActivity extends AppCompatActivity {

    private ActivityImageDetailsBinding activityImageDetailsBinding;
    private PhotoDetails photoDetails;
    private boolean isGoogle;
    private ImagesViewModel imagesViewModel;
    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityImageDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_details);
        activityImageDetailsBinding.setLifecycleOwner(this);
        imageLoader = new ImageLoader(getApplicationContext());
        getBundleData();
        initComponents();
    }

    private void initComponents() {
        imagesViewModel = new ViewModelProvider(this).get(ImagesViewModel.class);
        imagesViewModel.init(imageLoader);
        imagesViewModel.loading.set(View.VISIBLE);
        activityImageDetailsBinding.setViewmodel(imagesViewModel);
        activityImageDetailsBinding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        imagesViewModel.fetchPhotoDetails(photoDetails.id, isGoogle);
        imagesViewModel.getPhotoDetails().observe(this, new Observer<PhotoDetails>() {
            @Override
            public void onChanged(PhotoDetails photoDetails) {
                imagesViewModel.loading.set(View.GONE);
                activityImageDetailsBinding.setModel(photoDetails);
            }
        });
    }

    private void getBundleData() {
        if (getIntent() != null && getIntent().hasExtra("PHOTO_DETAILS")) {
            photoDetails = getIntent().getParcelableExtra("PHOTO_DETAILS");
        }
        if (getIntent() != null && getIntent().hasExtra("IS_GOOGLE")) {
            isGoogle = getIntent().getBooleanExtra("IS_GOOGLE", false);
        }

        if (photoDetails == null) {
            AppUtils.showToast(this, "Data is not available");
            finish();
        }
    }

}