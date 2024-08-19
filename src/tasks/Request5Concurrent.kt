package tasks

import contributors.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

suspend fun loadContributorsConcurrent(service: GitHubService, req: RequestData): List<User> = coroutineScope {
    val repos = service
        .getOrgReposCall(req.org)
        .also { logRepos(req, it) }
        .bodyList()

    val deferredListOfUsers = repos.map { repo ->
        async {
            log("starting loading for ${repo.name}")
            service.getRepoContributorsCall(req.org, repo.name)
                .also { logUsers(repo, it) }
                .bodyList()
        }
    }

    val list = deferredListOfUsers.awaitAll()
    list.flatten().aggregate()
}
