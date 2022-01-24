package mycorda.app.fileBundle


class BinaryBundleItemTest : BaseBundleItemTest() {
    override fun buildBundleItem(path: String, isExecutable: Boolean): BundleItem {
        return BinaryBundleItem(path, "foo".toByteArray(), isExecutable)
    }
}