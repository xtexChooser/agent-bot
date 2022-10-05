package ml.xtexx.agentBot.behaviour.park

import dev.inmo.tgbotapi.extensions.api.send.send
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onNewChatMembers
import mu.KotlinLogging

object XtexPark {

    val logger = KotlinLogging.logger("xtex's Park")

    suspend fun BehaviourContext.buildWelcome() {
        onNewChatMembers {
            it.chatEvent.members.forEach { user ->
                send(it.chat, "@${user.id.chatId} 欢迎，@xtexChooser，@xtexb，@xtexb。/hightech 查看更多功能")
            }
        }
    }

}