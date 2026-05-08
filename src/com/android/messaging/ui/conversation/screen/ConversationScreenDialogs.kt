package com.android.messaging.ui.conversation.screen

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import com.android.messaging.R
import com.android.messaging.ui.conversation.screen.model.ConversationAttachmentLimitWarning
import com.android.messaging.ui.conversation.screen.model.ConversationMessageDeleteConfirmationUiState
import com.android.messaging.ui.conversation.screen.model.ConversationScreenScaffoldUiState

@Composable
internal fun ConversationScreenDialogs(
    uiState: ConversationScreenScaffoldUiState,
    screenModel: ConversationScreenModel,
) {
    uiState.attachmentLimitWarning?.let { warning ->
        ConversationAttachmentLimitWarningDialog(
            warning = warning,
            onDismiss = screenModel::dismissAttachmentLimitWarning,
            onSendAnyway = screenModel::sendAnywayAfterAttachmentLimitWarning,
        )
    }

    uiState.selection.deleteConfirmation?.let { deleteConfirmation ->
        ConversationDeleteMessagesDialog(
            deleteConfirmation = deleteConfirmation,
            onConfirm = screenModel::confirmDeleteSelectedMessages,
            onDismiss = screenModel::dismissDeleteMessageConfirmation,
        )
    }

    if (uiState.isDeleteConversationConfirmationVisible) {
        ConversationDeleteConversationDialog(
            onConfirm = screenModel::confirmDeleteConversation,
            onDismiss = screenModel::dismissDeleteConversationConfirmation,
        )
    }
}

@Composable
private fun ConversationAttachmentLimitWarningDialog(
    warning: ConversationAttachmentLimitWarning,
    onDismiss: () -> Unit,
    onSendAnyway: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.mms_attachment_limit_reached),
            )
        },
        text = {
            Text(
                text = stringResource(
                    id = when (warning) {
                        ConversationAttachmentLimitWarning.ComposingAttachmentLimitReached -> {
                            R.string.attachment_limit_reached_dialog_message_when_composing
                        }

                        ConversationAttachmentLimitWarning.SendingMessageLimitReached -> {
                            R.string.attachment_limit_reached_dialog_message_when_sending
                        }

                        ConversationAttachmentLimitWarning.SendingVideoAttachmentLimitReached -> {
                            R.string.video_attachment_limit_exceeded_when_sending
                        }
                    },
                ),
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(android.R.string.ok),
                )
            }
        },
        dismissButton = when (warning) {
            ConversationAttachmentLimitWarning.SendingMessageLimitReached -> {
                {
                    TextButton(onClick = onSendAnyway) {
                        Text(
                            text = stringResource(R.string.attachment_limit_reached_send_anyway),
                        )
                    }
                }
            }

            ConversationAttachmentLimitWarning.ComposingAttachmentLimitReached,
            ConversationAttachmentLimitWarning.SendingVideoAttachmentLimitReached,
            -> null
        },
    )
}

@Composable
private fun ConversationDeleteConversationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = pluralStringResource(
                    id = R.plurals.delete_conversations_confirmation_dialog_title,
                    count = 1,
                    1,
                ),
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.delete_conversation_confirmation_button))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.delete_conversation_decline_button))
            }
        },
    )
}

@Composable
private fun ConversationDeleteMessagesDialog(
    deleteConfirmation: ConversationMessageDeleteConfirmationUiState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = pluralStringResource(
                    id = R.plurals.delete_messages_confirmation_dialog_title,
                    count = deleteConfirmation.messageIds.size,
                    deleteConfirmation.messageIds.size,
                ),
            )
        },
        text = {
            Text(
                text = stringResource(R.string.delete_message_confirmation_dialog_text),
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
            ) {
                Text(
                    text = stringResource(R.string.delete_message_confirmation_button),
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(
                    text = stringResource(android.R.string.cancel),
                )
            }
        },
    )
}
