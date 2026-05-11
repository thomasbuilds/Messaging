package com.android.messaging.ui.conversation.composer.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class ConversationSegmentCounterUiState(
    val codePointsRemainingInCurrentMessage: Int,
    val messageCount: Int,
)
