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

import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.skydoves.androidveil.VeilLayout

@BindingAdapter("loadImage")
fun bindLoadImage(view: AppCompatImageView, url: String) {
  Glide.with(view.context)
    .load(url)
    .into(view)
}

@BindingAdapter("withVeil", "loadImageWithVeil")
fun bindLoadImageWithVeil(view: AppCompatImageView, veilLayout: VeilLayout, url: String) {
  Glide.with(view.context)
    .load(url)
    .listener(object : RequestListener<Drawable> {
      override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<Drawable>?,
        isFirstResource: Boolean
      ): Boolean {
        veilLayout.unVeil()
        return false
      }

      override fun onResourceReady(
        resource: Drawable?,
        model: Any?,
        target: Target<Drawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
      ): Boolean {
        veilLayout.unVeil()
        return false
      }
    })
    .into(view)
}

@BindingAdapter("onBackPressed")
fun bindOnBackPressed(view: View, activity: AppCompatActivity) {
  view.setOnClickListener { activity.onBackPressed() }
}
