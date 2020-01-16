package tasks

import contributors.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

fun loadContributorsReactive(
    service: GitHubService,
    req: RequestData,
    scheduler: Scheduler = Schedulers.io()
): Single<List<User>> {
    val repos: Observable<Repo> = service
        .getOrgReposRx(req.org)
        .subscribeOn(scheduler)
        .doOnNext { response -> logRepos(req, response) }
        .flatMapIterable { it.bodyList() }

    val allUsers: Single<List<User>> = repos
        .flatMap { repo ->
            service.getRepoContributorsRx(req.org, repo.name)
                .subscribeOn(scheduler)
                .doOnNext { response -> logUsers(repo, response) }
        }
        .flatMapIterable { it.bodyList() }
        .toList()

    return allUsers
        .map { it.aggregate() }
}