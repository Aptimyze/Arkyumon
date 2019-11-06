/*
 *  Copyright (C) 2019
 *
 *  Licensed under the MIT License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      https://spit.com/license/apache-v2
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License
 */

package com.android.arkyumon.ui.about;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.arkyumon.ViewModelProviderFactory;
import com.android.arkyumon.data.remote.ApiClient;
import com.android.arkyumon.data.remote.ApiInterface;
import com.android.arkyumon.ui.base.BaseFragment;
import com.android.arkyumon.BR;
import com.android.arkyumon.R;
import com.android.arkyumon.databinding.FragmentAboutBinding;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * Created by CodersClan on 09/07/17.
 */

public class AboutFragment extends BaseFragment<FragmentAboutBinding, AboutViewModel> implements AboutNavigator {

    public static final String TAG = AboutFragment.class.getSimpleName();
    @Inject
    ViewModelProviderFactory factory;
    private FragmentAboutBinding binding;
    private LinearLayout toolbar;
    private AboutViewModel mAboutViewModel;
    private static final int IMG_REQUEST = 777;
    private Bitmap bitmap;
    private Context context;
    private File targetFile;
    private ImageView image;
    private Button submit;
    private InputStream in;
    private float latitude;
    private float longitude;
    private ScrollView scrollView;
    private ExifInterface exifInterface;


    public static AboutFragment newInstance() {
        Bundle args = new Bundle();
        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_about;
    }

    @Override
    public AboutViewModel getViewModel() {
        mAboutViewModel = ViewModelProviders.of(this,factory).get(AboutViewModel.class);
        return mAboutViewModel;
    }

    @Override
    public void goBack() {
        getBaseActivity().onFragmentDetached(TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        //binding = getViewDataBinding();
        //image = (ImageView)findViewById(R.id.targetimage);
        //submit = binding.submit;
        mAboutViewModel.setNavigator(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = getViewDataBinding();
        image = binding.potholeImage;
        submit = binding.submit;
        toolbar = binding.toolbar;
    }

    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMG_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMG_REQUEST && resultCode==RESULT_OK && data!=null){

            Uri path = data.getData();
            try {
                bitmap = BitmapFactory.decodeStream(context.getContentResolver()
                        .openInputStream(path));
                image.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);

                //bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), path);
                //binding.potholeImage.setImageBitmap(bitmap);

                //binding.potholeImage.setVisibility(View.VISIBLE);
                //binding.submit.setVisibility(View.VISIBLE);
                //binding.submit.setEnabled(true);
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                in = getActivity().getContentResolver().openInputStream(path);
                exifInterface = new ExifInterface(in);

                String exif="Exif: " + in;
                float[] LatLong = new float[2];
                if(exifInterface.getLatLong(LatLong)){
                    latitude = LatLong[0];
                    longitude = LatLong[1];
                    Log.d(TAG, "Lat and long are" + latitude + longitude);
                }else{
                    exif += "Exif tags are not available!";
                }
                Log.d("LatLong", exif);
                Toast.makeText(getActivity(), exif, Toast.LENGTH_SHORT ).show();

            } catch (IOException e) {
                // Handle any errors
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException ignored) {}
                }
            }

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            bitmap = rotateBitmap(bitmap, orientation);
            image.setImageBitmap(bitmap);
        }
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {

        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public void uploadImage() {
        String image = ImageToString();
        String complain = binding.complainText.toString();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        /*Call<PotholeComplain>  call = apiInterface.uploadImage(complain, image, );

        call.enqueue(new retrofit2.Callback<PotholeComplain>() {
            @Override
            public void onResponse(Call<PotholeComplain> call, Response<PotholeComplain> response) {
                PotholeComplain potholeComplain = response.body();
                Toast.makeText(getContext(), "Server Response" + potholeComplain.getResponse(), Toast.LENGTH_SHORT);
                binding.potholeImage.setVisibility(View.VISIBLE);
                binding.submit.setEnabled(false);
            }

            @Override
            public void onFailure(Call<PotholeComplain> call, Throwable t) {

            }
        });*/
    }

    private String ImageToString() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] imgByte = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

}
