package com.android.messaging.domain.conversation.usecase.draft

import com.android.messaging.data.conversation.model.draft.ConversationDraftAttachment
import com.android.messaging.data.conversation.model.metadata.ConversationSubscription
import com.android.messaging.data.conversation.repository.ConversationSubscriptionsRepository
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ResolveDraftAttachmentsWithinLimitTest {

    @Test
    fun invoke_addsUntilLimitAndDropsOverflow() {
        val currentAttachments = listOf(
            attachment(contentUri = "content://attachment/1"),
        )
        val attachmentsToAdd = listOf(
            attachment(contentUri = "content://attachment/2"),
            attachment(contentUri = "content://attachment/3"),
            attachment(contentUri = "content://attachment/4"),
        )

        val result = createResolveDraftAttachmentsWithinLimit(attachmentLimit = 3)(
            currentAttachments = currentAttachments,
            attachmentsToAdd = attachmentsToAdd,
        )

        assertEquals(
            listOf(
                attachment(contentUri = "content://attachment/2"),
                attachment(contentUri = "content://attachment/3"),
            ),
            result.attachmentsToAdd,
        )
        assertTrue(result.didDropAttachments)
    }

    @Test
    fun invoke_ignoresDuplicatesWithoutWarning() {
        val currentAttachments = listOf(
            attachment(contentUri = "content://attachment/1"),
        )
        val attachmentsToAdd = listOf(
            attachment(contentUri = "content://attachment/1"),
        )

        val result = createResolveDraftAttachmentsWithinLimit(attachmentLimit = 1)(
            currentAttachments = currentAttachments,
            attachmentsToAdd = attachmentsToAdd,
        )

        assertEquals(emptyList<ConversationDraftAttachment>(), result.attachmentsToAdd)
        assertFalse(result.didDropAttachments)
    }

    @Test
    fun invoke_exactLimitDoesNotWarn() {
        val currentAttachments = listOf(
            attachment(contentUri = "content://attachment/1"),
        )
        val attachmentsToAdd = listOf(
            attachment(contentUri = "content://attachment/2"),
        )

        val result = createResolveDraftAttachmentsWithinLimit(attachmentLimit = 2)(
            currentAttachments = currentAttachments,
            attachmentsToAdd = attachmentsToAdd,
        )

        assertEquals(attachmentsToAdd, result.attachmentsToAdd)
        assertFalse(result.didDropAttachments)
    }

    private fun attachment(contentUri: String): ConversationDraftAttachment {
        return ConversationDraftAttachment(
            contentType = "image/jpeg",
            contentUri = contentUri,
        )
    }

    private fun createResolveDraftAttachmentsWithinLimit(
        attachmentLimit: Int,
    ): ResolveDraftAttachmentsWithinLimit {
        return ResolveDraftAttachmentsWithinLimitImpl(
            conversationSubscriptionsRepository = FakeConversationSubscriptionsRepository(
                attachmentLimit = attachmentLimit,
            ),
        )
    }

    private class FakeConversationSubscriptionsRepository(
        private val attachmentLimit: Int,
    ) : ConversationSubscriptionsRepository {

        override fun observeActiveSubscriptions(): Flow<ImmutableList<ConversationSubscription>> {
            return emptyFlow()
        }

        override fun resolveAttachmentLimit(): Int {
            return attachmentLimit
        }

        override fun resolveMaxMessageSize(selfParticipantId: String): Flow<Int> {
            return emptyFlow()
        }
    }
}
