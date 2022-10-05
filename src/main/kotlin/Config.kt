package ml.xtexx.agentBot

import dev.inmo.tgbotapi.utils.telegramBotAPIDefaultUrl
import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val token: String,
    val api: String = telegramBotAPIDefaultUrl,
)
