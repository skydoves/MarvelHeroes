/*
 * Designed and developed by 2020 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.marvelheroes.repository

import androidx.annotation.WorkerThread
import com.skydoves.marvelheroes.mapper.ErrorResponseMapper
import com.skydoves.marvelheroes.model.Poster
import com.skydoves.marvelheroes.model.PosterErrorResponse
import com.skydoves.marvelheroes.network.MarvelClient
import com.skydoves.marvelheroes.persistence.PosterDao
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ResponseDataSource
import com.skydoves.sandwich.disposables.CompositeDisposable
import com.skydoves.sandwich.map
import com.skydoves.sandwich.message
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.suspendOnError
import com.skydoves.sandwich.suspendOnException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import timber.log.Timber

class MainRepository constructor(
  private val marvelClient: MarvelClient,
  private val marvelDataSource: ResponseDataSource<List<Poster>>,
  private val posterDao: PosterDao
) : Repository {

  init {
    Timber.d("Injection MainRepository")
  }

  @WorkerThread
  @ExperimentalCoroutinesApi
  fun loadMarvelPosters(
    disposable: CompositeDisposable,
    coroutineScope: CoroutineScope,
    error: (String?) -> Unit
  ) = callbackFlow {
    val posters = posterDao.getPosterList()
    if (posters.isEmpty()) {
      /**
       * fetch [Poster] from the network and getting [ApiResponse] asynchronously.
       * @see [onSuccess](https://github.com/skydoves/sandwich#onsuccess-onerror-onexception)
       * */
      marvelClient.fetchMarvelPosters(marvelDataSource, disposable, coroutineScope) { apiResponse ->
        apiResponse
          // handle the case when the API request gets a success response.
          .suspendOnSuccess {
            posterDao.insertPosterList(data)
            send(data)
          }
          // handle the case when the API request gets an error response.
          // e.g. internal server error.
          .suspendOnError {
            /** maps the [ApiResponse.Failure.Error] to the [PosterErrorResponse] using the mapper. */
            map(ErrorResponseMapper) { error("[Code: $code]: $message") }
          }
          // handle the case when the API request gets an exception response.
          // e.g., network connection error.
          .suspendOnException { error(message()) }
        close()
      }
    } else {
      send(posters)
      close()
    }
    awaitClose()
  }.flowOn(Dispatchers.IO)
}
