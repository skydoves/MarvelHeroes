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

package com.skydoves.marvelheroes.binding

import android.graphics.Color
import android.view.View
import android.widget.Toast
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.marvelheroes.extensions.circularRevealedAtCenter
import com.skydoves.marvelheroes.model.Poster
import com.skydoves.marvelheroes.model.PosterDetails
import com.skydoves.marvelheroes.view.adapter.PosterAdapter
import com.skydoves.marvelheroes.view.adapter.PosterSeriesAdapter
import com.skydoves.whatif.whatIfNotNull
import com.skydoves.whatif.whatIfNotNullAs
import com.skydoves.whatif.whatIfNotNullOrEmpty
import com.yarolegovich.discretescrollview.DiscreteScrollView
import com.yarolegovich.discretescrollview.transform.ScaleTransformer

object RecyclerViewBinding {
  @JvmStatic
  @BindingAdapter("adapter")
  fun bindAdapter(view: RecyclerView, baseAdapter: RecyclerView.Adapter<*>) {
    view.adapter = baseAdapter
  }

  @JvmStatic
  @BindingAdapter("toast")
  fun bindToast(view: RecyclerView, text: String?) {
    text.whatIfNotNullOrEmpty {
      Toast.makeText(view.context, it, Toast.LENGTH_SHORT).show()
    }
  }

  @JvmStatic
  @BindingAdapter("adapterPosterList")
  fun bindAdapterPosterList(view: DiscreteScrollView, posters: List<Poster>?) {
    posters.whatIfNotNullOrEmpty { items ->
      view.adapter.whatIfNotNullAs<PosterAdapter> { adapter ->
        adapter.updatePosterList(items)
      }
    }
    view.setItemTransformer(
      ScaleTransformer.Builder()
        .setMaxScale(1.25f)
        .setMinScale(0.8f)
        .build()
    )
  }

  @JvmStatic
  @BindingAdapter("bindOnItemChanged", "bindOnItemChangedBackground")
  fun bindOnItemChanged(view: DiscreteScrollView, adapter: PosterAdapter, pointView: View) {
    view.addOnItemChangedListener { viewHolder, _ ->
      viewHolder?.adapterPosition.whatIfNotNull {
        pointView.circularRevealedAtCenter(Color.parseColor(adapter.getPoster(it).color))
      }
    }
  }

  @JvmStatic
  @BindingAdapter("adapterPosterSeries", "adapterPosterDetailsList")
  fun bindAdapterPosterDetailsList(
    view: RecyclerView,
    adapter: PosterSeriesAdapter,
    posters: List<PosterDetails>?
  ) {
    posters.whatIfNotNullOrEmpty {
      view.adapter = adapter.apply { updatePosterDetailsList(it) }
    }
  }
}
