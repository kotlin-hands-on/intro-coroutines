package contributors

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.defaultRequest
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.URLProtocol
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.Observable
//import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface GitHubService {
    suspend fun getOrgRepos(org: String): List<Repo>

    suspend fun getRepoContributors(owner: String, repo: String): List<User>
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Repo(
    val id: Long,
    val name: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class User(
    val login: String,
    val contributions: Int
)

data class RequestData(
    val username: String,
    val password: String,
    val org: String
)

fun createGithubService(username: String, password: String): GitHubService {
    val authToken = "Basic " + Base64.getEncoder().encode("$username:$password".toByteArray()).toString(Charsets.UTF_8)
    return object : GitHubService {
        private val client = HttpClient(CIO) {
            install(JsonFeature)
            install(Logging)
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.github.com"
                }
                headers {
                    append("Append", "application/vnd.github.v3+json")
                    append("Authorization", authToken)
                }
            }
        }

        override suspend fun getOrgRepos(org: String): List<Repo> =
            client.get(path = "orgs/$org/repos?per_page=100")

        override suspend fun getRepoContributors(owner: String, repo: String): List<User> =
            client.get(path = "repos/$owner/$repo/contributors?per_page=100")

    }
}