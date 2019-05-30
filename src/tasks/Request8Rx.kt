package tasks

import contributors.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

fun loadContributorsReactive(
    service: GitHubService,
    req: RequestData,
    callback: (List<User>, completed: Boolean) -> Unit
) {
    val repos: Observable<Repo> = service
        .getOrgReposRx(req.org)
        .subscribeOn(Schedulers.io())
        .doOnNext { response -> logRepos(req, response) }
        .flatMapIterable { it.bodyList() }

    val allUsers: Single<List<User>> = repos
        .flatMap { repo ->
            service.getRepoContributorsRx(req.org, repo.name)
                .subscribeOn(Schedulers.io())
                .doOnNext { response -> logUsers(repo, response) }
        }
        .flatMapIterable { it.bodyList() }
        .toList()

    allUsers
        .doOnSuccess { callback(it.aggregate(), true) }
        .subscribe()
}