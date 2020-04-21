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

package com.skydoves.marvelheroes.extensions

import android.view.View
import android.view.ViewAnimationUtils
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat
import kotlin.math.hypot

/** makes visible a view. */
fun View.visible() {
  visibility = View.VISIBLE
}

/** circular revealed at right-end of a view. */
fun View.circularRevealedAtCenter(@ColorInt color: Int) {
  val cx = 0
  val cy = left
  val finalRadius = hypot(width.toDouble(), height.toDouble())
  if (isAttachedToWindow) {
    ViewAnimationUtils.createCircularReveal(this, cx, cy, 0f, finalRadius.toFloat()).apply {
      DrawableCompat.setTint(background, color)
      visible()
      duration = 550
      start()
    }
  }
}
