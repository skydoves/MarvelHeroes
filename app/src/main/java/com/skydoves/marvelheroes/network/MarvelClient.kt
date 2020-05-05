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

import com.skydoves.marvelheroes.model.Poster
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.ResponseDataSource

class MarvelClient(private val marvelService: MarvelService) {

  fun fetchMarvelPosters(
    dataSource: ResponseDataSource<List<Poster>>,
    onResult: (response: ApiResponse<List<Poster>>) -> Unit
  ) {
    dataSource
      // retry fetching data 3 times with 5000 milli-seconds time interval when the request gets failure.
      .retry(3, 5000L)
      // combine network service to the data source.
      .combine(marvelService.fetchMarvelPosterList(), onResult)
      // request API network call asynchronously.
      // if the request is successful, the data source will hold the success data.
      // in the next request after success, returns the cached API response with data.
      .request()
  }
}
