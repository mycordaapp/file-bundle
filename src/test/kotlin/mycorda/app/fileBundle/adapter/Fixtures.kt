package mycorda.app.fileBundle.adapter

import mycorda.app.fileBundle.BinaryBundleItem
import mycorda.app.fileBundle.FileBundle
import mycorda.app.fileBundle.FileBundleBuilder
import mycorda.app.fileBundle.TextBundleItem
import mycorda.app.types.UniqueId

object Fixtures {

    fun helloWorldBundle(id: UniqueId = UniqueId.randomUUID()): FileBundle {
        return FileBundleBuilder()
            .withName("HelloWorldBundle")
            .withId(id)
            .addItem(TextBundleItem("greeting.txt", "Hello, world"))
            .build()
    }

    fun binaryBundle(id: UniqueId = UniqueId.randomUUID()): FileBundle {
        return FileBundleBuilder()
            .withName("BinaryBundle")
            .withId(id)
            .addItem(BinaryBundleItem("greeting.bin", "Hello, world".toByteArray()))
            .build()
    }

    fun exampleFiles(id: UniqueId = UniqueId.randomUUID()): FileBundle {
        return FileBundleBuilder()
            .withName("ExampleFiles")
            .withId(id)
            .addItem(TextBundleItem.fromResource("/examples/mobydick.txt", "mobydick.txt"))
            .addItem(BinaryBundleItem.fromResource("/examples/r3.svg", "r3.svg"))
            .build()
    }
}