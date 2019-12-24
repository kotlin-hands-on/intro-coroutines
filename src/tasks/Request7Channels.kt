package tasks

import contributors.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import java.util.concurrent.atomic.AtomicInteger

suspend fun loadContributorsChannels(
    service: GitHubService,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit
) {
//    coroutineScope {
//        val repos = service.getOrgRepos(req.org)
//            .also { logRepos(req, it) }
//            .body() ?: emptyList()
//
//        val channel = produce {
//            for (repo in repos){
//                launch {
//                    val users = service.getRepoContributors(req.org, repo.name)
//                        .also { logUsers(repo, it) }
//                        .bodyList()
//                    send(users)
//                }
//            }
//        }
//
//        var allUsers = emptyList<User>()
//        repeat(repos.size) {
//            val users = channel.receive()
//            allUsers = (allUsers + users).aggregate()
//            updateResults(allUsers, it == repos.lastIndex)
//        }
//    }
    coroutineScope {
        val repos = service.getOrgRepos(req.org)
//            .also { logRepos(req, it) }

        val channel = produce {
            for (repo in repos){
                launch {
                    val users = service.getRepoContributors(req.org, repo.name)
//                        .also { logUsers(repo, it) }
                    send(users)
                }
            }
        }

        var allUsers = emptyList<User>()
        repeat(repos.size) {
            val users = channel.receive()
            allUsers = (allUsers + users).aggregate()
            updateResults(allUsers, it == repos.lastIndex)
        }
    }
}
