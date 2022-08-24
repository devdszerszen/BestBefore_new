package pl.dszerszen.bestbefore.ui.inapp


sealed class InAppMessage(open val status: MessageStatus) {

    class SnackBar(
        val title: String,
        val desc: String?,
        override val status: MessageStatus
    ) : InAppMessage(status)

    class BottomSheet(
        val title: String,
        val desc: String?,
        override val status: MessageStatus
    ) : InAppMessage(status)

    class Toast(
        val message: String,
    ) : InAppMessage(MessageStatus.INFO)
}

enum class MessageStatus {
    SUCCESS, INFO, ERROR
}