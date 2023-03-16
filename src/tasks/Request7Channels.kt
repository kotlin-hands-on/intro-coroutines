package tasks

import contributors.*
import kotlinx.coroutines.coroutineScope

suspend fun loadContributorsChannels(
    service: GitHubService,
    req: RequestData,
    updateResults: suspend (List<User>, completed: Boolean) -> Unit,
) {
    coroutineScope {
        TODO()
    }
}
