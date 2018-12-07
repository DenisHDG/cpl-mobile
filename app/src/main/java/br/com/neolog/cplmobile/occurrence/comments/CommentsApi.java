package br.com.neolog.cplmobile.occurrence.comments;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface CommentsApi {


    @Multipart
    @POST("monitoring-business-rules/occurrence/{occurrenceId}/comment")
    Call<Integer> createOccurrenceCommentMessage(
            @Part("occurrenceId") int id,
            @Part MultipartBody.Part attachments);

//    @GET("monitoring/comment/occurrence/{occurrenceId}")
//    Call<RestComment> getCommentsByOccurrence(@Path("occurrenceId") int id);=
//    @GET("monitoring/occurrence/{occurrenceId}")
//    Call<RestComment> getCommentsByOccurrence(
//            @Path("occurrenceId") int id);
}


