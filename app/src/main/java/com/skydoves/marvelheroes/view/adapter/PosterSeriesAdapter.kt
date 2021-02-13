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

package com.skydoves.marvelheroes.view.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.bindables.binding
import com.skydoves.marvelheroes.R
import com.skydoves.marvelheroes.databinding.ItemPosterSeriesBinding
import com.skydoves.marvelheroes.databinding.LayoutPlotBinding
import com.skydoves.marvelheroes.model.PosterDetails

class PosterSeriesAdapter(
  private val layoutPlotBinding: LayoutPlotBinding
) : RecyclerView.Adapter<PosterSeriesAdapter.PosterSeriesViewHolder>() {

  private val items = mutableListOf<PosterDetails>()
  private var selectable = true

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): PosterSeriesViewHolder {
    val binding = parent.binding<ItemPosterSeriesBinding>(R.layout.item_poster_series)
    return PosterSeriesViewHolder(binding).apply {
      binding.root.setOnClickListener {
        val position =
          adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return@setOnClickListener
        if (selectable) {
          selectable = false
          layoutPlotBinding.details = items[position]
          layoutPlotBinding.root.setOnClickListener {
            binding.transformationLayout.finishTransform()
            selectable = true
          }
          layoutPlotBinding.executePendingBindings()
          binding.transformationLayout.bindTargetView(layoutPlotBinding.root)
          binding.transformationLayout.startTransform()
        }
      }
    }
  }

  fun updatePosterDetailsList(posterDetails: List<PosterDetails>) {
    items.clear()
    items.addAll(posterDetails)
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int = items.size

  override fun onBindViewHolder(
    holder: PosterSeriesViewHolder,
    position: Int
  ) {
    holder.binding.apply {
      details = items[position]
      executePendingBindings()
    }
  }

  class PosterSeriesViewHolder(val binding: ItemPosterSeriesBinding) :
    RecyclerView.ViewHolder(binding.root)
}
