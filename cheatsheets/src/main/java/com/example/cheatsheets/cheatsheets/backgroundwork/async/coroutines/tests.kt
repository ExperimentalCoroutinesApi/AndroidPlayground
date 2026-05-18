package com.example.cheatsheets.cheatsheets.backgroundwork.async.coroutines

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.internal.ChannelFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.EmptyCoroutineContext


data class PostInput(
    val description: String,
    val localImages: List<String>,
)

data class Post(
    val id: String,
    val description: String,
    val remoteImages: List<String>,
)

interface PostsDataSource {

    suspend fun getPosts(): List<Post>

    suspend fun uploadImage(localImage: String): String

    suspend fun uploadPost(description: String, remoteImages: List<String>): String
}

class PostsRepository(
    private val dataSource: PostsDataSource,
) {
    private val _cache = MutableStateFlow<List<Post>>(emptyList())
    private val cache = _cache.asStateFlow()
    private var cacheTime = -1L
    fun observePosts(): Flow<List<Post>> {
        return cache
    }


    suspend fun refreshPosts() {
        val time = System.currentTimeMillis()
        if (time - cacheTime > 5000) {
            _cache.value = dataSource.getPosts()
            cacheTime = time
        }
    }

    suspend fun uploadPost(input: PostInput): String? {
        return try {
            coroutineScope {
                val remoteImages =
                    input.localImages.map { async { dataSource.uploadImage(it) } }.awaitAll()
                dataSource.uploadPost(input.description, remoteImages)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Throwable) {
            null
        }
    }
}










