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

package com.skydoves.marvelheroes.view.ui.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.skydoves.marvelheroes.R
import com.skydoves.marvelheroes.base.DatabindingActivity
import com.skydoves.marvelheroes.databinding.ActivityPosterDetailBinding
import com.skydoves.marvelheroes.extensions.onTransformationEndContainerApplyParams
import com.skydoves.marvelheroes.model.Poster
import com.skydoves.marvelheroes.view.adapter.PosterSeriesAdapter
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import org.koin.android.viewmodel.ext.android.getViewModel

class PosterDetailActivity : DatabindingActivity() {

  private val binding: ActivityPosterDetailBinding by binding(R.layout.activity_poster_detail)

  override fun onCreate(savedInstanceState: Bundle?) {
    onTransformationEndContainerApplyParams()
    super.onCreate(savedInstanceState)
    val poster =
      getViewModel<PosterDetailViewModel>().getPoster(intent.getLongExtra(posterId, 0))
    binding.apply {
      adapter = PosterSeriesAdapter(plot)
      this.poster = poster
    }
  }

  companion object {
    private const val posterId = "posterId"
    fun startActivity(
      context: Context,
      transformationLayout: TransformationLayout,
      poster: Poster
    ) {
      val intent = Intent(context, PosterDetailActivity::class.java)
      intent.putExtra(posterId, poster.id)
      TransformationCompat.startActivity(transformationLayout, intent)
    }
  }
}
