package com.cgc.kotlindemo

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.io.File

/**
 * author: CHENGC
 * date:2020/3/8 20:55
 * describe:
 */

interface GitHubApi {
    @GET("/repos/{owner}/{repo}")
    fun getRepository(@Path("owner") owner: String, @Path("repo") repo: String): Call<Repository>
}

fun main() {
    val gitHubApi = Retrofit.Builder().baseUrl("https://api.github.com")
        .addConverterFactory(GsonConverterFactory.create()).build()
        .create(GitHubApi::class.java)

    val response = gitHubApi.getRepository("JetBrains", "Kotlin").execute()

    val repository = response.body()

    if (null == repository) {
        println("Error!${response.code()}-${response.message()}")
    } else {
        println(repository.name)
        println(repository.owner.login)
        println(repository.stargazers_count)
        println(repository.forks_count)
        println(repository.html_url)



        File("Kotlin.html").writeText(
            """
            |无法访问此网站网址为 https://www.google.com/search?q=JsonToKotlin&oq=JsonToKotlin&aqs=chrome..69i57.295j0j1&sourceid=chrome&ie=UTF-8 的网页可能暂时无法连接，或者它已永久性地移动到了新网址。
ERR_QUIC_PROTOCOL_ERROR
        """.trimMargin()
        )


        val html = File("Kotlin.html").readText()

        println(html)
    }
}