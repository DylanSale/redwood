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
package app.cash.redwood.tooling.codegen

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.UNIT

internal object Protocol {
  val ChildrenTag = ClassName("app.cash.redwood.protocol", "ChildrenTag")
  val Diff = ClassName("app.cash.redwood.protocol", "Diff")
  val Event = ClassName("app.cash.redwood.protocol", "Event")
  val EventTag = ClassName("app.cash.redwood.protocol", "EventTag")
  val EventSink = ClassName("app.cash.redwood.protocol", "EventSink")
  val Id = ClassName("app.cash.redwood.protocol", "Id")
  val LayoutModifiers = ClassName("app.cash.redwood.protocol", "LayoutModifiers")
  val LayoutModifierElement = ClassName("app.cash.redwood.protocol", "LayoutModifierElement")
  val LayoutModifierTag = ClassName("app.cash.redwood.protocol", "LayoutModifierTag")
  val PropertyDiff = ClassName("app.cash.redwood.protocol", "PropertyDiff")
  val PropertyTag = ClassName("app.cash.redwood.protocol", "PropertyTag")
  val WidgetTag = ClassName("app.cash.redwood.protocol", "WidgetTag")
}

internal object ComposeProtocol {
  val ProtocolBridge = ClassName("app.cash.redwood.protocol.compose", "ProtocolBridge")
  val ProtocolBridgeFactory = ProtocolBridge.nestedClass("Factory")
  val ProtocolState = ClassName("app.cash.redwood.protocol.compose", "ProtocolState")
  val ProtocolMismatchHandler =
    ClassName("app.cash.redwood.protocol.compose", "ProtocolMismatchHandler")
  val ProtocolWidget = ClassName("app.cash.redwood.protocol.compose", "ProtocolWidget")
}

internal object WidgetProtocol {
  val ProtocolMismatchHandler =
    ClassName("app.cash.redwood.protocol.widget", "ProtocolMismatchHandler")
  val ProtocolNode = ClassName("app.cash.redwood.protocol.widget", "ProtocolNode")
  val ProtocolNodeFactory = ProtocolNode.nestedClass("Factory")
}

internal object Redwood {
  val LayoutModifier = ClassName("app.cash.redwood", "LayoutModifier")
  val LayoutModifierElement = LayoutModifier.nestedClass("Element")
  val LayoutScopeMarker = ClassName("app.cash.redwood", "LayoutScopeMarker")
  val RedwoodCodegenApi = ClassName("app.cash.redwood", "RedwoodCodegenApi")
  val OptInToRedwoodCodegenApi = AnnotationSpec.builder(Stdlib.OptIn)
    .addMember("%T::class", RedwoodCodegenApi)
    .build()
}

internal object RedwoodTesting {
  val MutableWidget = ClassName("app.cash.redwood.compose.testing", "MutableWidget")
  val RedwoodTester = ClassName("app.cash.redwood.compose.testing", "RedwoodTester")
  val WidgetValue = ClassName("app.cash.redwood.compose.testing", "WidgetValue")
}

internal object RedwoodWidget {
  val Widget = ClassName("app.cash.redwood.widget", "Widget")
  val WidgetChildren = Widget.nestedClass("Children")
  val WidgetChildrenOfW = WidgetChildren.parameterizedBy(typeVariableW)
  val WidgetProvider = Widget.nestedClass("Provider")
  val MutableListChildren = ClassName("app.cash.redwood.widget", "MutableListChildren")
}

internal object RedwoodCompose {
  val RedwoodComposeNode = MemberName("app.cash.redwood.compose", "RedwoodComposeNode")
}

internal object ComposeRuntime {
  val Composable = ClassName("androidx.compose.runtime", "Composable")
  val Stable = ClassName("androidx.compose.runtime", "Stable")
}

internal fun composableLambda(
  receiver: TypeName?,
): TypeName {
  return LambdaTypeName.get(
    returnType = UNIT,
    receiver = receiver,
  ).copy(
    annotations = listOf(
      AnnotationSpec.builder(ComposeRuntime.Composable).build(),
    ),
  )
}

internal object Stdlib {
  val AssertionError = ClassName("kotlin", "AssertionError")
  val List = ClassName("kotlin.collections", "List")
  val OptIn = ClassName("kotlin", "OptIn")
  val buildList = MemberName("kotlin.collections", "buildList")
  val listOf = MemberName("kotlin.collections", "listOf")
}

internal val typeVariableW = TypeVariableName("W", listOf(ANY))

internal object KotlinxSerialization {
  val Contextual = ClassName("kotlinx.serialization", "Contextual")
  val ContextualSerializer = ClassName("kotlinx.serialization", "ContextualSerializer")
  val ExperimentalSerializationApi = ClassName("kotlinx.serialization", "ExperimentalSerializationApi")
  val KSerializer = ClassName("kotlinx.serialization", "KSerializer")
  val Serializable = ClassName("kotlinx.serialization", "Serializable")
  val serializer = MemberName("kotlinx.serialization", "serializer")

  val SerialDescriptor = ClassName("kotlinx.serialization.descriptors", "SerialDescriptor")
  val buildClassSerialDescriptor = MemberName("kotlinx.serialization.descriptors", "buildClassSerialDescriptor")
  val element = MemberName("kotlinx.serialization.descriptors", "element")

  val Decoder = ClassName("kotlinx.serialization.encoding", "Decoder")
  val Encoder = ClassName("kotlinx.serialization.encoding", "Encoder")

  val Json = ClassName("kotlinx.serialization.json", "Json")
  val JsonDefault = Json.nestedClass("Default")
  val JsonPrimitive = MemberName("kotlinx.serialization.json", "JsonPrimitive")
  val jsonBoolean = MemberName("kotlinx.serialization.json", "boolean")

  @JvmField val jsonPrimitive = MemberName("kotlinx.serialization.json", "jsonPrimitive")
}

internal object KotlinxCoroutines {
  val CoroutineScope = ClassName("kotlinx.coroutines", "CoroutineScope")
}
