package com.cctpl.agroplannet.Notification;

import com.squareup.okhttp.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import com.squareup.okhttp.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAGrpdftg:APA91bFPHGhq3kaS5-nE3Centm9hVnDQ4JWietqmgV-KS8uyPmG61vpT37ZTZu1Rr-iyrV0Ux3vT16F6_tHTrW7u1mVTQ69qoAXz5ggEOWcP-Tm1iciJs9_2gDf8pVcd43vzRJ0NCtb3"
    })

    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body NotificationSender body);
}
