/*
 * Copyright (C) 2021 Square, Inc.
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
package app.cash.redwood.protocol.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MonotonicFrameClock
import app.cash.redwood.compose.LocalWidgetVersion
import app.cash.redwood.compose.RedwoodComposition
import app.cash.redwood.compose.WidgetApplier
import app.cash.redwood.protocol.DiffSink
import kotlinx.coroutines.CoroutineScope

/**
 * @param scope A [CoroutineScope] whose [coroutineContext][kotlin.coroutines.CoroutineContext]
 * must have a [MonotonicFrameClock] key which is being ticked.
 */
public fun ProtocolRedwoodComposition(
  scope: CoroutineScope,
  bridge: ProtocolBridge,
  diffSink: DiffSink,
  widgetVersion: UInt,
): RedwoodComposition {
  val applier = WidgetApplier(bridge.provider, bridge.root) {
    bridge.createDiffOrNull()?.let(diffSink::sendDiff)
  }
  val composition = RedwoodComposition(scope, applier)
  return ProtocolRedwoodComposition(composition, widgetVersion)
}

private class ProtocolRedwoodComposition(
  private val composition: RedwoodComposition,
  private val widgetVersion: UInt,
) : RedwoodComposition by composition {
  override fun setContent(content: @Composable () -> Unit) {
    composition.setContent {
      CompositionLocalProvider(LocalWidgetVersion provides widgetVersion) {
        content()
      }
    }
  }
}
