package tasks

import contributors.*
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.toObservable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

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