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

package com.skydoves.marvelheroes.network

import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.skydoves.marvelheroes.model.Poster
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ResponseDataSource
import com.skydoves.sandwich.disposables.CompositeDisposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.instanceOf
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.EmptyCoroutineContext

class MarvelServiceTest : ApiAbstract<MarvelService>() {

  private lateinit var service: MarvelService
  private val client: MarvelClient = mock()

  @Before
  fun initService() {
    service = createService(MarvelService::class.java)
  }

  @Throws(IOException::class)
  @Test
  fun fetchMarvelPosterListTest() {
    enqueueResponse("/MarvelLists.json")

    val responseBody = requireNotNull(service.fetchMarvelPosterList().execute().body())
    mockWebServer.takeRequest()

    assertThat(responseBody[0].id, `is`(0L))
    assertThat(responseBody[0].name, `is`("Deadpool"))
    assertThat(responseBody[0].color, `is`("#770609"))

    val dataSource = ResponseDataSource<List<Poster>>()
    val onResult: suspend (response: ApiResponse<List<Poster>>) -> Unit = {
      assertThat(it, instanceOf(ApiResponse.Success::class.java))
      val response: List<Poster> = requireNotNull((it as ApiResponse.Success).data)
      assertThat(response[0].id, `is`(0L))
      assertThat(response[0].name, `is`("Deadpool"))
      assertThat(response[0].color, `is`("#770609"))
    }

    val compositeDisposable = CompositeDisposable()
    val scope = CoroutineScope(EmptyCoroutineContext + Dispatchers.IO)
    whenever(
      client.fetchMarvelPosters(dataSource, compositeDisposable, scope, onResult)
    ).thenAnswer {
      val response: suspend (response: ApiResponse<List<Poster>>) -> Unit = it.getArgument(3)
      scope.launch {
        response(ApiResponse.Success(Response.success(responseBody)))
      }
    }

    client.fetchMarvelPosters(dataSource, compositeDisposable, scope, onResult)
  }
}
