package com.e.agroplannet.Notification;

import com.squareup.okhttp.ResponseBody;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAEnIi6cI:APA91bGW0f_jiDPJoaALwTo7PJY5BYg_mjAAkfFKUOgm7kklZB8oFgROEvDWgsvwjYxz1VqSPVzN5PS1oUWfdbiq8FX8R8w_tXhLSSwM3wepgKQs7fEsaRRHxJ_NSKZJc_hxmGhVy0Ue"
    })

    @POST("fcm/send")
    Call<ResponseBody> sendNotification(@Body NotificationSender body);
}
