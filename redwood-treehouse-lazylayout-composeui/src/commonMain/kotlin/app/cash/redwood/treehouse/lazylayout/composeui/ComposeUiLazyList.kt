/*
 * Copyright (C) 2022 Square, Inc.
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
package app.cash.redwood.treehouse.lazylayout.composeui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.cash.redwood.LayoutModifier
import app.cash.redwood.treehouse.AppService
import app.cash.redwood.treehouse.TreehouseApp
import app.cash.redwood.treehouse.TreehouseView.WidgetSystem
import app.cash.redwood.treehouse.composeui.TreehouseContent
import app.cash.redwood.treehouse.lazylayout.api.LazyListInterval
import app.cash.redwood.treehouse.lazylayout.widget.LazyList

internal class ComposeUiLazyList<A : AppService>(
  treehouseApp: TreehouseApp<A>,
  widgetSystem: WidgetSystem,
) : LazyList<@Composable () -> Unit> {
  private var isVertical by mutableStateOf(false)
  private var intervals by mutableStateOf<List<LazyListInterval>>(emptyList())

  override var layoutModifiers: LayoutModifier = LayoutModifier

  override fun isVertical(isVertical: Boolean) {
    this.isVertical = isVertical
  }

  override fun intervals(intervals: List<LazyListInterval>) {
    this.intervals = intervals
  }

  override val value = @Composable {
    // TODO Remove statically sized containers (https://github.com/cashapp/redwood/pull/854).
    val itemBoxModifier = if (isVertical) Modifier.height(64.dp) else Modifier.width(64.dp)
    val content: LazyListScope.() -> Unit = {
      intervals.forEach { interval ->
        items(
          count = interval.keys.size,
          key = { index -> interval.keys[index] ?: index },
        ) { index ->
          Box(itemBoxModifier) {
            TreehouseContent(treehouseApp, widgetSystem) { interval.itemProvider.get(index) }
          }
        }
      }
    }
    if (isVertical) {
      LazyColumn(
        modifier = Modifier
          .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content,
      )
    } else {
      LazyRow(
        modifier = Modifier
          .fillMaxHeight(),
        verticalAlignment = Alignment.CenterVertically,
        content = content,
      )
    }
  }
}
