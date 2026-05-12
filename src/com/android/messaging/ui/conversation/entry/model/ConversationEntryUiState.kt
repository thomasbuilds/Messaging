package com.android.messaging.ui.conversation.entry.model

import androidx.compose.runtime.Immutable
import com.android.messaging.data.conversation.model.draft.ConversationDraft
import com.android.messaging.ui.conversation.composer.model.ConversationSimSelectorUiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
internal data class ConversationEntryUiState(
    val launchGeneration: Int? = null,
    val conversationId: String? = null,
    val isCreatingGroup: Boolean = false,
    val isResolvingConversation: Boolean = false,
    val isResolvingConversationIndicatorVisible: Boolean = false,
    val pendingDraft: ConversationDraft? = null,
    val pendingScrollPosition: Int? = null,
    val pendingSelfParticipantId: String? = null,
    val pendingStartupAttachment: ConversationEntryStartupAttachment? = null,
    val resolvingRecipientDestination: String? = null,
    val selectedGroupRecipientDestinations: ImmutableList<String> = persistentListOf(),
    val simSelectorState: ConversationSimSelectorUiState = ConversationSimSelectorUiState(),
)

@Immutable
internal data class ConversationEntryStartupAttachment(
    val contentType: String,
    val contentUri: String,
)
