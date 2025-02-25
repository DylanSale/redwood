/*
 * Copyright (C) 2023 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package app.cash.redwood.layout

import app.cash.redwood.LayoutModifier
import app.cash.redwood.widget.Widget
import org.junit.Test

abstract class AbstractSpacerTest<T : Any> {

  abstract fun widget(
    width: Int = 0,
    height: Int = 0,
    layoutModifier: LayoutModifier = LayoutModifier,
  ): Widget<T>

  abstract fun wrap(widget: Widget<T>, horizontal: Boolean): T

  abstract fun verifySnapshot(value: T)

  @Test fun zeroSpacer() {
    val widget = widget(width = 0, height = 0)
    verifySnapshot(wrap(widget, horizontal = true))
  }

  @Test fun widthOnlySpacer() {
    val widget = widget(width = 100, height = 0)
    verifySnapshot(wrap(widget, horizontal = true))
  }

  @Test fun heightOnlySpacer() {
    val widget = widget(width = 0, height = 100)
    verifySnapshot(wrap(widget, horizontal = false))
  }

  @Test fun bothSpacer() {
    val widget = widget(width = 100, height = 100)
    verifySnapshot(wrap(widget, horizontal = false))
  }
}
