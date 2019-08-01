package tasks

import contributors.User

fun List<User>.aggregate(): List<User> = groupBy(User::login)
    .map { (_, group) -> group.first().copy(contributions = group.sumBy(User::contributions)) }
    .sortedByDescending(User::contributions)