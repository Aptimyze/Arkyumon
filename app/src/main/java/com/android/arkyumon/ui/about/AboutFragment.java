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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.android.arkyumon.ui.login.LoginActivity;
import com.android.arkyumon.ui.main.MainActivity;
import com.android.arkyumon.utils.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

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
    private final static int MY_PERMISSION_REQUEST= 100;
    private Uri path;


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

            path = data.getData();
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
                Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT ).show();

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


    public void uploadImage(){
        RequestBody desciption = RequestBody.create(MultipartBody.FORM, binding.complainText.getText().toString() );
        File originalFile = FileUtils.getFile(context, path);

        RequestBody filepart = RequestBody.create(
                MediaType.parse(context.getContentResolver().getType(path)), originalFile);

        MultipartBody.Part file = MultipartBody.Part.createFormData("photo", originalFile.getName(), filepart);
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://d39dc7e4.ngrok.io/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());


        Retrofit retrofit = builder.build();

        ApiInterface client =  retrofit.create(ApiInterface.class);

        Call<ResponseBody> call = client.uploadImage(desciption, file, latitude, longitude );
        call.enqueue(new retrofit2.Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Toast.makeText(context, "Yeah", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
