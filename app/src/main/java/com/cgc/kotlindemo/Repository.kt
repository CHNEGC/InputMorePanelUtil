package com.cgc.kotlindemo

/**
 * author: CHENGC
 * date:2020/3/8 22:58
 * describe:
 */
data class Repository(
    val name: String,
    val forks_count: String,
    val stargazers_count: String,
    val owner: Owner,
    val html_url: String
) {


    data class Owner(val login: String) {

    }
}