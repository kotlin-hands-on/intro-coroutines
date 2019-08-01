package tasks

import contributors.GitHubService
import contributors.Repo
import contributors.RequestData
import contributors.User
import contributors.logRepos
import contributors.logUsers

suspend fun loadContributorsProgress(
    service: GitHubService,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit
) {
    val allUsers = mutableListOf<User>()
    service.getOrgRepos(req.org)
        .also { logRepos(req, it) }
        .bodyList()
        .let { repos ->
            repos.forEachIndexed { index, repo ->
                getRepoContributors(service, req, repo) { repoUsers ->
                    updateResults(allUsers.aggregateUsers(repoUsers), index == repos.lastIndex)
                }
            }
        }

}

private suspend fun getRepoContributors(service: GitHubService, req: RequestData, repo: Repo, callback: suspend (List<User>) -> Unit) {
    service.getRepoContributors(req.org, repo.name)
        .also { logUsers(repo, it) }
        .bodyList()
        .apply { callback(this) }
}

private fun MutableList<User>.aggregateUsers(users: List<User>): List<User> = run {
    this += users
    aggregate()
}