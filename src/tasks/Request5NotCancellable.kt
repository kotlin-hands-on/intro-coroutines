package tasks

import contributors.GitHubService
import contributors.RequestData
import contributors.User

suspend fun loadContributorsNotCancellable(service: GitHubService, req: RequestData): List<User> {
    TODO()
}
