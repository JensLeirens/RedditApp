package mafken.redditapp.network;

import mafken.redditapp.callData.Return;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Calls {

    @GET("r/{subreddit}.json")
    Call<Return> getSubreddits(@Path("subreddit") String subreddit, @Query("after") String after, @Query("limit") int limit);

}
