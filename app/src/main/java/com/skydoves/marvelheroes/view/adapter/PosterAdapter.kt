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
import com.skydoves.marvelheroes.databinding.ItemPosterBinding
import com.skydoves.marvelheroes.model.Poster
import com.skydoves.marvelheroes.view.ui.details.PosterDetailActivity

class PosterAdapter : RecyclerView.Adapter<PosterAdapter.PosterViewHolder>() {

  private val items = mutableListOf<Poster>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
    val binding = parent.binding<ItemPosterBinding>(R.layout.item_poster)
    return PosterViewHolder(binding).apply {
      binding.root.setOnClickListener { view ->
        val position =
          adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return@setOnClickListener
        PosterDetailActivity.startActivity(
          view.context,
          binding.transformationLayout,
          items[position]
        )
      }
    }
  }

  fun updatePosterList(posters: List<Poster>) {
    items.clear()
    items.addAll(posters)
    notifyDataSetChanged()
  }

  override fun getItemCount(): Int = items.size

  override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
    holder.binding.apply {
      poster = items[position]
      veil = itemVeilLayout
      executePendingBindings()
    }
  }

  fun getPoster(index: Int): Poster = items[index]

  class PosterViewHolder(val binding: ItemPosterBinding) :
    RecyclerView.ViewHolder(binding.root)
}
