package com.aprosoft.apfwakotlin.Retrofit;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class RetrofitUtils {

    private static final String MIME_TYPE_TEXT = "text/plain";
    private static final String MIME_TYPE_IMAGE = "image/*";
    private static final String MIME_TYPE_FILE = "multipart/form-data";

    public static RequestBody StringtoRequestBody(String string){
        return RequestBody.create(MediaType.parse(MIME_TYPE_TEXT), string);
    }

    public static RequestBody fileToRequestBody(File file){
        return RequestBody.create(MediaType.parse(MIME_TYPE_IMAGE), file);
    }
}

