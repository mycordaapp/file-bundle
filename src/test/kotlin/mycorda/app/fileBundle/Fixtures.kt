package mycorda.app.fileBundle

import mycorda.app.fileBundle.builders.FileBundleBuilder
import mycorda.app.types.UniqueId

object Fixtures {

    fun helloWorldBundle(id: UniqueId = UniqueId.randomUUID()): FileBundle {
        return FileBundleBuilder()
            .withName("HelloWorldBundle")
            .withId(id)
            .addItem(TextBundleItem("greeting.txt", "Hello, world"))
            .build()
    }

    fun mixOfLineTerminationBundle(id: UniqueId = UniqueId.randomUUID()): FileBundle {
        return FileBundleBuilder()
            .withName("MixOfLineTerminationBundle")
            .withId(id)
            .addItem(TextBundleItem("unix.txt", "Hello,\n world"))
            .addItem(TextBundleItem("windows.txt", "Hello,\r\n world"))
            .addItem(TextBundleItem("trailing-unix.txt", "Hello, world\n"))
            .addItem(TextBundleItem("trailing-windows.txt", "Hello, world\r\n"))
            .build()
    }

    fun binaryBundle(id: UniqueId = UniqueId.randomUUID()): FileBundle {
        return FileBundleBuilder()
            .withName("BinaryBundle")
            .withId(id)
            .addItem(BinaryBundleItem("greeting.bin", "Hello, world".toByteArray(),true))
            .build()
    }


    fun allExampleFiles(id: UniqueId = UniqueId.randomUUID()): FileBundle {
        return FileBundleBuilder()
            .withName("ExampleFiles")
            .withId(id)
            .addItem(TextBundleItem.fromResource("/examples/mobydick.txt", "mobydick.txt"))
            .addItem(BinaryBundleItem.fromResource("/examples/r3.svg", "r3.svg"))
            .addItem(TextBundleItem("this/is/along/path.txt", "foo"))
            .addItem(TextBundleItem("trailing-new-line.txt", "foo\n"))
            .addItem(TextBundleItem.fromResource("/examples/verylongline.txt", "verylongline.txt"))
            .addItem(TextBundleItem.fromResource("/examples/LICENCE", "LICENCE"))
            .addItem(TextBundleItem.fromResource("/examples/foo.sh", "foo.sh", true))
            .build()
    }
}