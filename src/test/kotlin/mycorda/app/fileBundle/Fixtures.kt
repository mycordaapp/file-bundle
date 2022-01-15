package mycorda.app.fileBundle

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

    fun allExampleFiles(id: UniqueId = UniqueId.randomUUID()): FileBundle {
        return FileBundleBuilder()
            .withName("ExampleFiles")
            .withId(id)
            .addItem(TextBundleItem.fromResource("/examples/mobydick.txt", "mobydick.txt"))
            .addItem(BinaryBundleItem.fromResource("/examples/r3.svg", "r3.svg"))
            .addItem(TextBundleItem("this/is/along/path.txt", "foo"))
            .addItem(TextBundleItem.fromResource("/examples/verylongline.txt","verylongline.txt"))
            .build()
    }
}