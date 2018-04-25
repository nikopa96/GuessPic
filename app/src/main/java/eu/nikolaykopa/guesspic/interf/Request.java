package eu.nikolaykopa.guesspic.interf;

import java.util.List;

import eu.nikolaykopa.guesspic.model.Variant;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Request.
 */

public interface Request {
    @GET("/output/request_main.php")
    Call<List<Variant>> getData(@Query("lang") String lang, @Query("resolution") String resolution, @Query("cat") String cat, @Query("level") String level);

    @GET("/output/request_start.php")
    Call<List<Variant>> getStarterData(@Query("lang") String lang, @Query("resolution") String resolution, @Query("cat") String cat);
}
