package id.calocallo.githubusersapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class UsersVMFactory(private val repository: RemoteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(UsersViewModel::class.java) -> {
                return UsersViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("ERROR UNKNOWN VIEWMODEL CLASS")
        }
    }
}