package tasks

import contributors.*
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

suspend fun loadContributorsChannels(
    service: GitHubService,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit
) {
    coroutineScope {
        val channel = Channel<List<User>>()
        val allUsers = mutableListOf<User>()
        service.getOrgRepos(req.org)
            .also { logRepos(req, it) }
            .bodyList()
            .also { repos ->
                launch {
                    repeat(repos.size) { index ->
                        val repoUsers = channel.receive()
                        allUsers += repoUsers
                        updateResults(allUsers.aggregate(), repos.lastIndex == index)
                    }
                }
            }.forEach { repo ->
                launch {
                    service.getRepoContributors(req.org, repo.name)
                        .also { logUsers(repo, it) }
                        .bodyList()
                        .let { repoUsers -> channel.send(repoUsers) }
                }
            }
    }
}
