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

import Foundation
import UIKit
import EmojiSearchKt

class EmojiSearchViewController : UIViewController {

    // MARK: - Private Properties

    private let urlSession: URLSession = .init(configuration: .default)

    // MARK: - UIViewController

    override func viewDidLoad() {
        super.viewDidLoad()

        view.backgroundColor = .white
    }

    override func loadView() {
        let emojiSearchLauncher = EmojiSearchLauncher(nsurlSession: urlSession, hostApi: IosHostApi())
        let treehouseApp = emojiSearchLauncher.createTreehouseApp()
        let widgetSystem = EmojiSearchWidgetSystem(treehouseApp: treehouseApp)
        let treehouseView = TreehouseUIKitView(widgetSystem: widgetSystem)
        let content = treehouseApp.createContent(
            source: EmojiSearchContent(),
            codeListener: CodeListener()
        )
        ExposedKt.bindWhenReady(content: content, view: treehouseView)
        view = treehouseView.view
    }
}

class EmojiSearchContent : TreehouseContentSource {
    func get(app: AppService) -> ZiplineTreehouseUi {
        let treehouesUi = (app as! EmojiSearchPresenter)
        return treehouesUi.launch()
    }
}

class EmojiSearchWidgetSystem : TreehouseViewWidgetSystem {
    let treehouseApp: TreehouseApp<EmojiSearchPresenter>

    init(treehouseApp: TreehouseApp<EmojiSearchPresenter>) {
        self.treehouseApp = treehouseApp
    }

    func widgetFactory(
        json: Kotlinx_serialization_jsonJson,
        protocolMismatchHandler: ProtocolMismatchHandler
    ) -> ProtocolNodeFactory {
        return ProtocolEmojiSearchProtocolNodeFactory<UIView>(
            provider: WidgetEmojiSearchWidgetFactories<UIView>(
                EmojiSearch: IosEmojiSearchWidgetFactory(treehouseApp: treehouseApp, widgetSystem: self),
                RedwoodLayout: UIViewRedwoodLayoutWidgetFactory(),
                RedwoodTreehouseLazyLayout: UIViewRedwoodTreehouseLazyLayoutWidgetFactory(
                    treehouseApp: treehouseApp,
                    widgetSystem: self
                )
            ),
            json: json,
            mismatchHandler: protocolMismatchHandler
        );
    }
}
