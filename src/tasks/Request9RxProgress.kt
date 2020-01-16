package tasks

import contributors.*
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

fun loadContributorsReactiveProgress(
    service: GitHubService,
    req: RequestData,
    scheduler: Scheduler = Schedulers.io()
): Observable<List<User>> {
    val repos: Observable<Repo> = service
        .getOrgReposRx(req.org)
        .subscribeOn(scheduler)
        .doOnNext { response -> logRepos(req, response) }
        .flatMapIterable { response -> response.bodyList() }

    val repoUsers: Observable<List<User>> = repos
        .flatMap { repo ->
            service.getRepoContributorsRx(req.org, repo.name)
                .subscribeOn(scheduler)
                .doOnNext { response -> logUsers(repo, response) }
                .map { response -> response.bodyList() }
        }

    var allUsers = emptyList<User>()

    return repoUsers.map { users: List<User> ->
        allUsers = (allUsers + users).aggregate()
        allUsers
    }
}