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
package app.cash.redwood.tooling.codegen

import app.cash.redwood.tooling.schema.LayoutModifier
import app.cash.redwood.tooling.schema.Schema
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec

internal fun generateLayoutModifierInterface(schema: Schema, layoutModifier: LayoutModifier): FileSpec {
  val type = schema.layoutModifierType(layoutModifier)
  return FileSpec.builder(type.packageName, type.simpleName)
    .addType(
      TypeSpec.interfaceBuilder(type)
        .addSuperinterface(Redwood.LayoutModifierElement)
        .apply {
          layoutModifier.deprecation?.let { deprecation ->
            addAnnotation(deprecation.toAnnotationSpec())
          }
          for (property in layoutModifier.properties) {
            addProperty(
              PropertySpec.builder(property.name, property.type.asTypeName())
                .apply {
                  property.deprecation?.let { deprecation ->
                    addAnnotation(deprecation.toAnnotationSpec())
                  }
                }
                .build(),
            )
          }
        }
        .build(),
    )
    .build()
}
