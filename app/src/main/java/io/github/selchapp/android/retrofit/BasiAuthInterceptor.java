package io.github.selchapp.android.retrofit;

/**
 * Created by rzetzsche on 30.09.17.
 */

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BasiAuthInterceptor implements Interceptor {

    private String credentials;

    public BasiAuthInterceptor(String user, String password) {
        this.credentials = Credentials.basic(user, password);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request authenticatedRequest = request.newBuilder()
                .header("Authorization", credentials).build();
        Response response = chain.proceed(authenticatedRequest);
        return response;
    }

}
