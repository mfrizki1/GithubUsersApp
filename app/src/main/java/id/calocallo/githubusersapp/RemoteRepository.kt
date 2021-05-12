package id.calocallo.githubusersapp

import id.calocallo.githubusersapp.model.UsersDto
import id.calocallo.githubusersapp.network.ApiService

class RemoteRepository(private val apiService: ApiService) {
    suspend fun listUsers(
        onResult: (MutableList<UsersDto>?) -> Unit,
        onError: (String) -> Unit
    ) {
        val response = apiService.users()
        if (response.isSuccessful) {
            onResult(response.body())
        } else {
            onError(response.message())
        }
    }
}