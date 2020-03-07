package contributors

import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Response
import retrofit2.mock.Calls

object MockGithubService : GitHubService {
    override suspend fun getOrgRepos(org: String): List<Repo> {
        return repos
    }

    override suspend fun getRepoContributors(owner: String, repo: String): List<User> {
        return reposMap.getValue(repo).users
    }

}