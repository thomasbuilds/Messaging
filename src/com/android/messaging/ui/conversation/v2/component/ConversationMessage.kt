package com.android.messaging.ui.conversation.v2.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.android.messaging.ui.conversation.v2.model.ConversationMessagePartUiModel
import com.android.messaging.ui.conversation.v2.model.ConversationMessageUiModel

@Composable
internal fun ConversationMessage(
    modifier: Modifier = Modifier,
    message: ConversationMessageUiModel,
) {
    val horizontalArrangement = if (message.isIncoming) {
        Arrangement.Start
    } else {
        Arrangement.End
    }
    val bubbleColor = if (message.isIncoming) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }
    val bubbleShape = messageBubbleShape(message = message)
    val messageBody = buildMessageBody(message = message)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = horizontalArrangement,
    ) {
        Surface(
            color = bubbleColor,
            shape = bubbleShape,
            modifier = Modifier.fillMaxWidth(fraction = 0.8f),
        ) {
            Column(
                modifier = Modifier.padding(all = 12.dp),
                verticalArrangement = Arrangement.spacedBy(space = 4.dp),
            ) {
                if (message.isIncoming && !message.senderDisplayName.isNullOrBlank()) {
                    Text(
                        text = message.senderDisplayName,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.primary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                Text(
                    text = messageBody,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

private fun messageBubbleShape(message: ConversationMessageUiModel): RoundedCornerShape {
    val cornerRadius = 20.dp
    val topCornerRadius = if (message.canClusterWithPrevious) {
        0.dp
    } else {
        cornerRadius
    }
    val bottomCornerRadius = if (message.canClusterWithNext) {
        0.dp
    } else {
        cornerRadius
    }

    return RoundedCornerShape(
        topStart = topCornerRadius,
        topEnd = topCornerRadius,
        bottomStart = bottomCornerRadius,
        bottomEnd = bottomCornerRadius,
    )
}

private fun buildMessageBody(message: ConversationMessageUiModel): String {
    val text = message.text?.takeIf { value ->
        value.isNotBlank()
    }
    if (text != null) {
        return text
    }

    val subject = message.mmsSubject?.takeIf { value ->
        value.isNotBlank()
    }
    if (subject != null) {
        return subject
    }

    val partText = message.parts.firstNotNullOfOrNull { part ->
        part.text?.takeIf { value ->
            value.isNotBlank()
        }
    }
    if (partText != null) {
        return partText
    }

    return message.parts.firstOrNull()?.contentType.orEmpty()
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
