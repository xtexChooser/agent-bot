package ml.xtexx.agentBot

import com.soywiz.korio.file.std.localCurrentDirVfs
import dev.inmo.tgbotapi.bot.TelegramBot
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
import ml.xtexx.agentBot.behaviour.dn42.buildWhois
import ml.xtexx.agentBot.behaviour.park.XtexPark
import ml.xtexx.agentBot.util.Updater
import mu.KotlinLogging
import mu.KotlinLoggingConfiguration
import mu.KotlinLoggingLevel

object Bot {

    val logger = KotlinLogging.logger("Bot")

    val fs = localCurrentDirVfs
    lateinit var config: Config
    lateinit var bot: TelegramBot

    fun start() {
        @OptIn(DelicateCoroutinesApi::class)
        GlobalScope.launch {
            KotlinLoggingConfiguration.LOG_LEVEL = KotlinLoggingLevel.DEBUG
            logger.info { "Starting bot" }
            config = Json.decodeFromString<Config>(fs["config.json"].readString())
            logger.info { "API Endpoint: ${config.api}" }
            bot = telegramBot(config.token, apiUrl = config.api)

            launch {
                Updater.check()
            }

            launch {
                val self = bot.getMe()
                logger.info { "Self: $self" }
            }

            launch {
                logger.info { "Updating commands" }
                bot.setMyCommands(
                    BotCommand("identity", "查看聊天 ID"),
                    BotCommand("search_engine", "善 用 搜 索"),
                    BotCommand("hightech", "快速发消息菜单（群聊）未来高科技（私聊）"),
                    BotCommand("whois", "查询 DN42 的 WHOIS 信息"),
                )
                logger.info { "Commands updated" }
            }

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
                buildWhois()
            }.join()
        }
    }

}