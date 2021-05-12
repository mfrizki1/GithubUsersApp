package id.calocallo.githubusersapp.network

import id.calocallo.githubusersapp.model.UsersDto
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("users")
    suspend fun users() : Response<MutableList<UsersDto>>

}