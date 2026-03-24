package com.android.messaging.ui.conversation.v2.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.android.messaging.ui.conversation.v2.model.ConversationMessagePartUiModel
import com.android.messaging.ui.conversation.v2.model.ConversationMessageUiModel

@Composable
internal fun ConversationMessages(
    modifier: Modifier = Modifier,
    messages: List<ConversationMessageUiModel>,
    listState: LazyListState,
) {
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        contentPadding = PaddingValues(all = 16.dp),
    ) {
        itemsIndexed(
            items = messages,
            key = { _, message -> message.messageId },
        ) { index, message ->
            ConversationMessage(
                modifier = Modifier.padding(top = messageItemTopPadding(index = index, message = message)),
                message = message,
            )
        }
    }
}

private fun messageItemTopPadding(
    index: Int,
    message: ConversationMessageUiModel,
): Dp {
    if (index == 0) {
        return 0.dp
    }

    return if (message.canClusterWithPrevious) {
        2.dp
    } else {
        8.dp
    }
}


private fun previewMessage(
    messageId: String,
    text: String,
    isIncoming: Boolean,
    senderDisplayName: String?,
    canClusterWithPrevious: Boolean,
    canClusterWithNext: Boolean,
): ConversationMessageUiModel {
    return ConversationMessageUiModel(
        messageId = messageId,
        conversationId = "preview-conversation",
        text = text,
        parts = listOf(
            ConversationMessagePartUiModel(
                contentType = "text/plain",
                text = text,
                contentUri = null,
                width = 0,
                height = 0,
            ),
        ),
        sentTimestamp = 0L,
        receivedTimestamp = 0L,
        status = if (isIncoming) {
            ConversationMessageUiModel.Status.Incoming.Complete
        } else {
            ConversationMessageUiModel.Status.Outgoing.Complete
        },
        isIncoming = isIncoming,
        senderDisplayName = senderDisplayName,
        senderAvatarUri = null,
        senderContactLookupKey = null,
        canClusterWithPrevious = canClusterWithPrevious,
        canClusterWithNext = canClusterWithNext,
        mmsSubject = null,
        protocol = ConversationMessageUiModel.Protocol.SMS,
    )
}
