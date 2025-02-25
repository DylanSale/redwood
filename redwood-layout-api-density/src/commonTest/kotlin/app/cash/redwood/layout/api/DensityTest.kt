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
package app.cash.redwood.layout.api

import kotlin.test.Test
import kotlin.test.assertEquals

class DensityTest {

  @Test fun dpToPxConversionIsSymmetric() {
    with(Density(2.0)) {
      var dp = 4.dp
      assertEquals(dp, dp.toPx().toDp())

      dp = 20.dp
      assertEquals(dp, dp.toPx().toDp())
    }
  }

  @Test fun pxToDpConversionIsSymmetric() {
    with(Density(2.0)) {
      var px = 4.0
      assertEquals(px, px.toDp().toPx())

      px = 20.0
      assertEquals(px, px.toDp().toPx())
    }
  }
}
