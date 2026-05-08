package com.android.messaging.domain.conversation.usecase.draft.model

import com.android.messaging.data.conversation.model.draft.ConversationDraftAttachment

internal data class DraftAttachmentLimitResult(
    val attachmentsToAdd: List<ConversationDraftAttachment>,
    val didDropAttachments: Boolean,
)
