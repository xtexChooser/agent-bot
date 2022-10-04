package ml.xtexx.agentBot.behaviour

import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.types.message.Markdown
import dev.inmo.tgbotapi.types.message.MarkdownV2
import ml.xtexx.agentBot.Bot
import ml.xtexx.agentBot.ChatConsts

suspend fun BehaviourContext.buildIdentity() {
    onCommand("identity") {
        val chatID = it.chat.id.chatId
        Bot.logger.info { "Reply identity request in $chatID" }
        reply(it, buildString {
            append("Chat ID: `${chatID}`\n")
            arrayOf(
                ChatConsts.Group::XTEX_PARK,
                ChatConsts.User::XTEX,
                ChatConsts.User::SEALCHANPS,
            ).forEach { prop ->
                if (chatID == prop.get()) append("Identified as ${prop.name}")
            }
        })
    }
}