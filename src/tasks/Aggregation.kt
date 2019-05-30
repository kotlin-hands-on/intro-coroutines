package tasks

import contributors.User

// TODO: Write aggregation code.
//  In the initial list each user is present several times, once for each
//  repository he or she contributed to.
//  Merge duplications: each user should be present only once in the resulting list
//  with the total value of contributions for all the repositories.
//  Users should be sorted in a descending order by their contributions.
fun List<User>.aggregate(): List<User> =
    this