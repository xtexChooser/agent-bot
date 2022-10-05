package ml.xtexx.agentBot

import dev.inmo.tgbotapi.extensions.api.bot.getMe
import dev.inmo.tgbotapi.extensions.api.bot.setMyCommands
import dev.inmo.tgbotapi.extensions.api.telegramBot
import dev.inmo.tgbotapi.extensions.behaviour_builder.buildBehaviourWithLongPolling
import dev.inmo.tgbotapi.types.BotCommand
import io.ktor.utils.io.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ml.xtexx.agentBot.behaviour.*
import ml.xtexx.agentBot.behaviour.park.XtexPark
import mu.KotlinLogging
import mu.KotlinLoggingConfiguration
import mu.KotlinLoggingLevel
import okio.NodeJsFileSystem
import okio.Path.Companion.toPath
import okio.buffer
import okio.use

object Bot {

    val logger = KotlinLogging.logger("Bot")

    val fs = NodeJsFileSystem
    val config = Json.decodeFromString<Config>(fs.source("config.json".toPath())
        .use { it.buffer().readUtf8() })
    val bot = telegramBot(config.token, apiUrl = config.api)

    fun start() {
        KotlinLoggingConfiguration.LOG_LEVEL = KotlinLoggingLevel.DEBUG
        logger.info { "Starting bot" }
        logger.info { "API Endpoint: ${config.api}" }
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch {
            val self = bot.getMe()
            bot.setMyCommands(
                BotCommand("identity", "查看聊天ID"),
                BotCommand("search_engine", "善 用 搜 索"),
                BotCommand("hightech", "快速发消息菜单（群聊）未来高科技（私聊）"),
            )
            logger.info { "Self: $self" }
            bot.buildBehaviourWithLongPolling(defaultExceptionsHandler = {
                var e: Throwable? = it
                while (e != null) {
                    if (e is CancellationException) {
                        return@buildBehaviourWithLongPolling
                    }
                    e = e.cause
                }
                logger.error(it) { "Error caught, type: ${it::class.simpleName}" }
                it.printStackTrace()
            }) {
                buildStart()
                buildIdentity()
                buildSearchEngine()
                buildForward()
                with(XtexPark) {
                    buildWelcome()
                }
                buildFaDianReplyKeyboard()
            }.join()
        }
    }

}