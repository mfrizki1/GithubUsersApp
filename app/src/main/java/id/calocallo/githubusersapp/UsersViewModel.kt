package id.calocallo.githubusersapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import id.calocallo.githubusersapp.model.UsersDto
import kotlinx.coroutines.launch

class UsersViewModel(private val repo: RemoteRepository) : ViewModel() {
    private val dataUsers = MutableLiveData<MutableList<UsersDto>>()
    val getDataUsers: LiveData<MutableList<UsersDto>> = dataUsers
    private val error = MutableLiveData<String>()
    val getError: LiveData<String> = error

    fun listUsers() = viewModelScope.launch {
        repo.listUsers({
            dataUsers.value = it
        }, {
            error.value = it
        })
    }
}