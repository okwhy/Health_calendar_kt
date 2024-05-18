import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("api/users/login")
    fun login(@Query("login") login: String,@Query("password") password: String): Call<Long>
}
