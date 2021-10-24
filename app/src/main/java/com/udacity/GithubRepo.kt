package com.udacity

enum class GithubRepo(val repoName: String, val url: String) {
    GLIDE("Glide", "https://github.com/bumptech/glide/archive/master.zip"),
    UDACITY(
        "Udacity Project 3",
        "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
    ),
    RETROFIT("Retrofit", "https://github.com/square/retrofit/archive/master.zip")
}