package com.android.messaging.data.subscription.model

import androidx.compose.runtime.Immutable
import com.android.messaging.data.conversation.model.metadata.ConversationSubscriptionLabel

@Immutable
internal data class Subscription(
    val selfParticipantId: String,
    val subId: Int,
    val label: ConversationSubscriptionLabel,
    val displayDestination: String?,
    val displaySlotId: Int,
    val color: Int,
)
