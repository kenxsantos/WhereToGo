package com.example.wheretogo

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateListOf

class PostViewModel : ViewModel() {

    private val _posts = mutableStateListOf<Post>()
    val posts: List<Post> get()  = _posts

    fun addPost(name: String, origin: String, destination: String, description: String) {
        val newPost = Post(id = _posts.size + 1, name = name, origin = origin, destination = destination, description = description)
        _posts.add(newPost)
    }

    fun removePost(post: Post) {
        _posts.remove(post)
    }

}