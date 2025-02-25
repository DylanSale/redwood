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
package app.cash.redwood.treehouse.lazylayout.view

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.cash.redwood.treehouse.AppService
import app.cash.redwood.treehouse.TreehouseApp
import app.cash.redwood.treehouse.TreehouseView.WidgetSystem
import app.cash.redwood.treehouse.lazylayout.widget.LazyList
import app.cash.redwood.treehouse.lazylayout.widget.RedwoodTreehouseLazyLayoutWidgetFactory

public class ViewRedwoodTreehouseLazyLayoutWidgetFactory<A : AppService>(
  private val context: Context,
  private val treehouseApp: TreehouseApp<A>,
  private val widgetSystem: WidgetSystem,
) : RedwoodTreehouseLazyLayoutWidgetFactory<View> {
  public override fun LazyList(): LazyList<View> = ViewLazyList(treehouseApp, widgetSystem, RecyclerView(context))
}
