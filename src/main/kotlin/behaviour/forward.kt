package ml.xtexx.agentBot.behaviour

import com.soywiz.klock.ISO8601
import dev.inmo.tgbotapi.extensions.api.forwardMessage
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.api.send.send
import dev.inmo.tgbotapi.extensions.api.send.sendActionTyping
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onContentMessage
import dev.inmo.tgbotapi.extensions.utils.asTextedInput
import dev.inmo.tgbotapi.types.ChatId
import dev.inmo.tgbotapi.types.chat.PrivateChat
import dev.inmo.tgbotapi.utils.PreviewFeature
import ml.xtexx.agentBot.Bot
import ml.xtexx.agentBot.ChatConsts

@OptIn(PreviewFeature::class)
suspend fun BehaviourContext.buildForward() {
    onContentMessage {
        if (it.chat is PrivateChat && it.content.asTextedInput()?.text?.startsWith('/') != true) {
            Bot.logger.info { "Trying to forward a message, $it" }
            sendActionTyping(it.chat)
            val forward = if (it.forwardable) {
                forwardMessage(ChatId(ChatConsts.User.XTEX), it)
            } else {
                val texted = it.content.asTextedInput()
                if (texted == null) {
                    reply(it, "Unsupported message type for forwarding/不支持的消息类型")
                    return@onContentMessage
                }
                send(ChatId(ChatConsts.User.XTEX), texted.textSources)
            }
            reply(forward, buildString {
                append("MsgID: ${it.messageId} ChatID: ${it.chat.id.chatId}/${(it.chat as PrivateChat).username?.username}\n")
                append("Date: ${it.date.format(ISO8601.DATETIME_UTC_COMPLETE)}\n")
                if (it.forwardInfo != null)
                    append("Forwarded: ${it.forwardInfo}")
                if (it.editDate != null)
                    append("Edited: ${it.editDate!!.format(ISO8601.DATETIME_UTC_COMPLETE)}")
                if (it.replyTo != null)
                    append("Reply to: ${it.replyTo!!.messageId}")
            }.trim())
            reply(it, "Sent/已发送")
        }
    }
}