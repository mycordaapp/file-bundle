package mycorda.app.fileBundle


class BinaryBundleItemTest : BaseBundleItemTest() {
    override fun buildBundleItem(path: String): BundleItem = BinaryBundleItem(path,"foo".toByteArray())

}