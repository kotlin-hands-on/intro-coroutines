package tasks

import contributors.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers

fun loadContributorsReactiveProgress(
    service: GitHubService,
    req: RequestData,
    callback: (List<User>, completed: Boolean) -> Unit
) {
    val repos: Observable<Repo> = service
        .getOrgReposRx(req.org)
        .subscribeOn(Schedulers.io())
        .doOnNext { response -> logRepos(req, response) }
        .flatMapIterable { response -> response.bodyList() }

    val repoUsers: Observable<List<User>> = repos
        .flatMap { repo ->
            service.getRepoContributorsRx(req.org, repo.name)
                .subscribeOn(Schedulers.io())
                .doOnNext { response -> logUsers(repo, response) }
                .map { response -> response.bodyList() }
        }
    repoUsers
        .reduce(listOf<User>()) { allUsers, users ->
            (allUsers + users).aggregate().also {
                callback(it, false)
            }
        }
        .doOnSuccess { callback(it, true) }
        .subscribe()
}