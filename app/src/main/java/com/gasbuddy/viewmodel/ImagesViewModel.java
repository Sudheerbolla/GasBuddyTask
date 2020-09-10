package com.gasbuddy.viewmodel;

import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSpinner;
import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;
import androidx.databinding.ObservableInt;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gasbuddy.R;
import com.gasbuddy.adapters.ImagesListAdapter;
import com.gasbuddy.imageloading.ImageLoader;
import com.gasbuddy.models.PhotoDetails;
import com.gasbuddy.utils.wsutils.FetchImageDetails;
import com.gasbuddy.utils.wsutils.FetchImagesList;

import java.util.ArrayList;
import java.util.List;

public class ImagesViewModel extends ViewModel {

    private ImagesListAdapter adapter;
    private FetchImagesList fetchImagesList;
    private FetchImageDetails fetchImageDetails;
    public MutableLiveData<PhotoDetails> selected;
    private ArrayList<PhotoDetails> imagesOriginalArrayList;
    public ObservableInt loading;
    private MutableLiveData<Integer> mode;
    public ObservableInt showEmpty;
    private static MutableLiveData<Boolean> useCache;
    public MutableLiveData<String> networkMethod;
    public MutableLiveData<String> source;
    private static ImageLoader imageLoader;

    public void init(ImageLoader imageLoader) {
        ImagesViewModel.imageLoader = imageLoader;
        selected = new MutableLiveData<>();
        fetchImageDetails = new FetchImageDetails();
        imagesOriginalArrayList = new ArrayList<>();
        fetchImagesList = new FetchImagesList();
        adapter = new ImagesListAdapter(R.layout.item_images, this);
        loading = new ObservableInt(View.GONE);
        mode = new MutableLiveData<>(0);
        showEmpty = new ObservableInt(View.GONE);
        useCache = new MutableLiveData<>(false);
        networkMethod = new MutableLiveData<>("Retrofit");
        source = new MutableLiveData<>("UnSplash");
    }

    public void fetchList() {
        fetchImagesList.fetchList(networkMethod.getValue(), source.getValue());
    }

    public MutableLiveData<List<PhotoDetails>> getPhotosList() {
        return fetchImagesList.getPhotosList();
    }

    public MutableLiveData<Boolean> getUseCache() {
        return useCache;
    }

    public MutableLiveData<Integer> getMode() {
        return mode;
    }

    public MutableLiveData<String> getNetworkMethod() {
        return networkMethod;
    }

    public MutableLiveData<String> getSource() {
        return source;
    }

    public void fetchPhotoDetails(String id) {
        fetchImageDetails.fetchPhotoDetails(id, networkMethod.getValue(), source.getValue());
    }

    public void fetchPhotoDetails(String id, boolean isGoogle) {
        fetchImageDetails.fetchPhotoDetails(id, networkMethod.getValue(), isGoogle ? "Google" : "UnSplash");
    }

    public MutableLiveData<PhotoDetails> getPhotoDetails() {
        return fetchImageDetails.getPhotoDetails();
    }

    public ImagesListAdapter getAdapter() {
        return adapter;
    }

    public void setPhotosListInAdapter(List<PhotoDetails> photoDetailsList) {
        if (photoDetailsList != null) this.adapter.setImagesList(photoDetailsList);
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    public void preserveOriginalData(List<PhotoDetails> photoDetailsList) {
        imagesOriginalArrayList = new ArrayList<>();
        imagesOriginalArrayList.addAll(photoDetailsList);
    }

    public MutableLiveData<PhotoDetails> getSelected() {
        return selected;
    }

    public void onItemClick(Integer index) {
        PhotoDetails db = getPhotoAt(index);
        selected.setValue(db);
    }

    public void onModeChange() {
        mode.setValue(mode.getValue() == 0 ? 1 : 0);
    }

    public void onUseCacheClick() {
        useCache.setValue(!useCache.getValue());
    }

    public PhotoDetails getPhotoAt(Integer index) {
        if (fetchImagesList.getPhotosList().getValue() != null && index != null &&
                fetchImagesList.getPhotosList().getValue().size() > index) {
            return fetchImagesList.getPhotosList().getValue().get(index);
        }
        return null;
    }

    public void performSearch(String text) {
        if (TextUtils.isEmpty(text)) {
            setPhotosListInAdapter(imagesOriginalArrayList);
        } else {
            ArrayList<PhotoDetails> imagesArrayList = new ArrayList<>();
            for (PhotoDetails photoDetails : imagesOriginalArrayList) {
                if ((!TextUtils.isEmpty(photoDetails.description) && photoDetails.description.toLowerCase().contains(text)) ||
                        (!TextUtils.isEmpty(photoDetails.altDescription) && photoDetails.altDescription.toLowerCase().contains(text))) {
                    imagesArrayList.add(photoDetails);
                } else if ((!TextUtils.isEmpty(photoDetails.volumeInfo.description) && photoDetails.volumeInfo.description.toLowerCase().contains(text)) ||
                        (!TextUtils.isEmpty(photoDetails.volumeInfo.title) && photoDetails.volumeInfo.title.toLowerCase().contains(text))) {
                    imagesArrayList.add(photoDetails);
                }
            }
            setPhotosListInAdapter(imagesArrayList);
        }
    }

    @BindingAdapter("setAdapter")
    public static void bindRecyclerViewAdapter(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @BindingAdapter("imageUrl")
    public static void bindRecyclerViewAdapter(ImageView imageView, PhotoDetails photoDetails) {
//        app:imageUrl="@{
//        viewModel.getPhotoAt(position).urls==null?
//        viewModel.getPhotoAt(position).volumeInfo.imageLinks.smallThumbnail:
//        viewModel.getPhotoAt(position).urls.thumb
//        }" />
        String imageUrl = "";
        if (photoDetails != null) {
            if (photoDetails.urls != null) {
                imageUrl = photoDetails.urls.thumb;
            } else if (photoDetails.volumeInfo != null) {
                imageUrl = photoDetails.volumeInfo.imageLinks.smallThumbnail;
            }
        }
        if (!TextUtils.isEmpty(imageUrl)) {
            if (imageView.getTag(R.id.image_url) == null || !imageView.getTag(R.id.image_url).equals(imageUrl)) {
                imageView.setImageBitmap(null);
                imageView.setTag(R.id.image_url, imageUrl);
                imageLoader.displayImage(imageUrl, useCache.getValue(), imageView);
            }
        } else {
            imageView.setTag(R.id.image_url, null);
            imageView.setImageBitmap(null);
        }
    }


    @BindingAdapter("imageUrlItem")
    public static void bindImage(ImageView imageView, PhotoDetails photoDetails) {
        String imageUrl = "";
        if (photoDetails != null) {
            if (photoDetails.urls != null) {
                imageUrl = photoDetails.urls.regular;
            } else if (photoDetails.volumeInfo != null) {
                imageUrl = photoDetails.volumeInfo.imageLinks.thumbnail;
            }
        }
        if (!TextUtils.isEmpty(imageUrl)) {
            if (imageView.getTag(R.id.image_url) == null || !imageView.getTag(R.id.image_url).equals(imageUrl)) {
                imageView.setImageBitmap(null);
                imageView.setTag(R.id.image_url, imageUrl);
                imageLoader.displayImage(imageUrl, useCache.getValue(), imageView);
            }
        } else {
            imageView.setTag(R.id.image_url, null);
            imageView.setImageBitmap(null);
        }
    }

    @BindingAdapter("description")
    public static void bindRecyclerViewAdapter(TextView textView, PhotoDetails photoDetails) {
        String desc = "";
        if (!TextUtils.isEmpty(photoDetails.description)) desc = photoDetails.description;
        else if (!TextUtils.isEmpty(photoDetails.altDescription))
            desc = photoDetails.altDescription;
        else if (photoDetails.volumeInfo != null && !TextUtils.isEmpty(photoDetails.volumeInfo.title))
            desc = photoDetails.volumeInfo.title;
        else if (photoDetails.volumeInfo != null && !TextUtils.isEmpty(photoDetails.volumeInfo.description))
            desc = photoDetails.volumeInfo.description;
        textView.setText(desc);
    }

    @BindingAdapter(value = {"selectedValue", "selectedValueAttrChanged"}, requireAll = false)
    public static void bindSpinnerData(AppCompatSpinner pAppCompatSpinner, String newSelectedValue, final InverseBindingListener newTextAttrChanged) {
        pAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newTextAttrChanged.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (newSelectedValue != null) {
            int pos = ((ArrayAdapter<String>) pAppCompatSpinner.getAdapter()).getPosition(newSelectedValue);
            pAppCompatSpinner.setSelection(pos, true);
        }
    }

    @BindingAdapter(value = {"selectedSource", "selectedSourceAttrChanged"}, requireAll = false)
    public static void bindSpinnerDataSource(AppCompatSpinner pAppCompatSpinner, String newSelectedValue, final InverseBindingListener newTextAttrChanged) {
        pAppCompatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                newTextAttrChanged.onChange();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if (newSelectedValue != null) {
            int pos = ((ArrayAdapter<String>) pAppCompatSpinner.getAdapter()).getPosition(newSelectedValue);
            pAppCompatSpinner.setSelection(pos, true);
        }
    }

    @InverseBindingAdapter(attribute = "selectedValue", event = "selectedValueAttrChanged")
    public static String captureSelectedValue(AppCompatSpinner pAppCompatSpinner) {
        return (String) pAppCompatSpinner.getSelectedItem();
    }

    @InverseBindingAdapter(attribute = "selectedSource", event = "selectedSourceAttrChanged")
    public static String captureSelectedValueSource(AppCompatSpinner pAppCompatSpinner) {
        return (String) pAppCompatSpinner.getSelectedItem();
    }

}
