package com.android.messaging.ui.conversation.screen.model

import androidx.compose.runtime.Immutable

@Immutable
internal sealed interface ConversationAttachmentLimitWarning {
    data object ComposingAttachmentLimitReached : ConversationAttachmentLimitWarning

    data object SendingMessageLimitReached : ConversationAttachmentLimitWarning

    data object SendingVideoAttachmentLimitReached : ConversationAttachmentLimitWarning
}
