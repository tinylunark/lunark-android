package com.example.lunark.services;

import com.example.lunark.models.ReviewReport;

import java.util.List;

import io.reactivex.Completable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReviewReportService {
   @GET("reports/reviews")
   @Headers({
           "User-Agent: Mobile-Android",
           "Content-Type:application/json"
   })
   Call<List<ReviewReport>> getReviewReports();

   @DELETE("reports/reviews/{id}")
   @Headers({
           "User-Agent: Mobile-Android",
           "Content-Type:application/json"
   })
   Call<ResponseBody> deleteReview(@Path("id") Long reviewId);

   @POST("reports/reviews")
   @Headers({
           "User-Agent: Mobile-Android",
           "Content-Type:application/json"
   })
   Completable createReport(@Body ReviewReport report);
}
