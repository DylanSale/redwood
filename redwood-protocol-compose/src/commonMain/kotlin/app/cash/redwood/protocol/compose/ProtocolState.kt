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
package app.cash.redwood.protocol.compose

import app.cash.redwood.protocol.ChildrenDiff
import app.cash.redwood.protocol.ChildrenTag
import app.cash.redwood.protocol.Diff
import app.cash.redwood.protocol.Id
import app.cash.redwood.protocol.LayoutModifiers
import app.cash.redwood.protocol.PropertyDiff
import app.cash.redwood.widget.Widget

/** @suppress For generated code use only. */
public class ProtocolState {
  private var nextValue = Id.Root.value + 1
  private val widgets = PlatformMap<Int, ProtocolWidget>()

  private var childrenDiffs = PlatformList<ChildrenDiff>()
  private var layoutModifiers = PlatformList<LayoutModifiers>()
  private var propertyDiffs = PlatformList<PropertyDiff>()
  private var hasDiffs = false

  public fun nextId(): Id {
    val value = nextValue
    nextValue = value + 1
    return Id(value)
  }

  public fun append(childrenDiff: ChildrenDiff) {
    childrenDiffs.add(childrenDiff)
    hasDiffs = true
  }

  public fun append(layoutModifiers: LayoutModifiers) {
    this.layoutModifiers.add(layoutModifiers)
    hasDiffs = true
  }

  public fun append(propertyDiff: PropertyDiff) {
    propertyDiffs.add(propertyDiff)
    hasDiffs = true
  }

  /**
   * If there were any calls to [append] since the last call to this function return them as a
   * [Diff] and reset the internal lists to be empty. This function returns null if there were
   * no calls to [append] since the last invocation.
   */
  public fun createDiffOrNull(): Diff? {
    if (!hasDiffs) return null

    val diff = Diff(
      childrenDiffs = childrenDiffs.asList(),
      layoutModifiers = layoutModifiers.asList(),
      propertyDiffs = propertyDiffs.asList(),
    )

    childrenDiffs = PlatformList()
    layoutModifiers = PlatformList()
    propertyDiffs = PlatformList()

    return diff
  }

  public fun addWidget(widget: ProtocolWidget) {
    val idValue = widget.id.value
    check(idValue !in widgets) {
      "Attempted to add widget with ID $idValue but one already exists"
    }
    widgets[idValue] = widget
  }

  public fun removeWidget(id: Id) {
    widgets.remove(id.value)
  }

  public fun getWidget(id: Id): ProtocolWidget? = widgets[id.value]

  public fun widgetChildren(id: Id, tag: ChildrenTag): Widget.Children<Nothing> {
    return ProtocolWidgetChildren(id, tag, this)
  }
}
