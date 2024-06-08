package com.oocode;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public class BasicHttpClient implements HttpClient {

    private final OkHttpClient okHttpClient;

    public BasicHttpClient() {
        okHttpClient = new OkHttpClient();
    }

    public String readUrl(String url) {
        Request request = new Request.Builder().url(url).build();
        try (Response response = okHttpClient.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                ResponseBody rb = response.body();
                return rb.string();
            } else {
                throw new RuntimeException("Reading url failed: " + url + ". Error code: " + response.code());
            }
        } catch (IOException e) {
            throw new RuntimeException("Reading url failed: " + url, e);
        }
    }

}
