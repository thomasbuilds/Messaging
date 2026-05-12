package com.android.messaging.domain.conversation.usecase.draft

import com.android.messaging.data.conversation.model.draft.ConversationDraftAttachment
import com.android.messaging.data.subscription.repository.SubscriptionsRepository
import com.android.messaging.domain.conversation.usecase.draft.model.DraftAttachmentLimitResult
import javax.inject.Inject

internal interface ResolveDraftAttachmentsWithinLimit {
    operator fun invoke(
        currentAttachments: Collection<ConversationDraftAttachment>,
        attachmentsToAdd: Collection<ConversationDraftAttachment>,
    ): DraftAttachmentLimitResult
}

internal class ResolveDraftAttachmentsWithinLimitImpl @Inject constructor(
    private val subscriptionsRepository: SubscriptionsRepository,
) : ResolveDraftAttachmentsWithinLimit {

    override operator fun invoke(
        currentAttachments: Collection<ConversationDraftAttachment>,
        attachmentsToAdd: Collection<ConversationDraftAttachment>,
    ): DraftAttachmentLimitResult {
        return resolveAttachmentsWithinLimit(
            currentAttachments = currentAttachments,
            attachmentsToAdd = attachmentsToAdd,
            attachmentLimit = subscriptionsRepository.resolveAttachmentLimit(),
        )
    }

    private fun resolveAttachmentsWithinLimit(
        currentAttachments: Collection<ConversationDraftAttachment>,
        attachmentsToAdd: Collection<ConversationDraftAttachment>,
        attachmentLimit: Int,
    ): DraftAttachmentLimitResult {
        val remainingAttachmentSlots = (attachmentLimit - currentAttachments.size)
            .coerceAtLeast(0)

        val currentAttachmentContentUris = currentAttachments
            .map { attachment -> attachment.contentUri }
            .toSet()

        val uniqueAttachmentsToAdd = attachmentsToAdd
            .asSequence()
            .distinctBy { attachment -> attachment.contentUri }
            .filterNot { attachment ->
                attachment.contentUri in currentAttachmentContentUris
            }
            .toList()

        val acceptedAttachments = uniqueAttachmentsToAdd
            .take(n = remainingAttachmentSlots)

        return DraftAttachmentLimitResult(
            attachmentsToAdd = acceptedAttachments,
            didDropAttachments = uniqueAttachmentsToAdd.size > acceptedAttachments.size,
        )
    }
}
