package mycorda.app.fileBundle


/*
 */
sealed class BundleItem(val path: String, val content: String)


class InMemoryBundleItem(path: String, content: String) : BundleItem(path, content)

