package ml.xtexx.agentBot.behaviour

import dev.inmo.micro_utils.coroutines.launchSafely
import dev.inmo.tgbotapi.extensions.api.delete
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.utils.types.buttons.*
import dev.inmo.tgbotapi.types.chat.PrivateChat
import dev.inmo.tgbotapi.utils.row
import kotlinx.coroutines.delay
import ml.xtexx.agentBot.Bot

suspend fun BehaviourContext.buildFaDianReplyKeyboard() {
    onCommand("hightech") {
        Bot.logger.info { "Someone requested fa dian keyboard in ${it.chat.id.chatId}" }
        val group = it.chat !is PrivateChat
        val replyMsg =
            reply(it, "欢迎使用 xtex Agent Bot 的 40 世纪先进功能", replyMarkup = buildFaDianReplyKeyboard(group))
        launchSafely {
            delay(5000L)
            delete(replyMsg)
            Bot.logger.info { "A fa dian keyboard in ${it.chat.id.chatId} removed" }
        }
    }
}

fun buildFaDianReplyKeyboard(group: Boolean) = replyKeyboard {
    if (group) {
        row {
            simpleButton("我是傻逼")
            simpleButton("你是傻逼")
            simpleButton("透我")
        }
        row {
            simpleButton("查我学历")
            simpleButton("超蓝我的笔")
            simpleButton("潮湿我")
        }
        row {
            simpleButton("茶包我")
            simpleButton("抢建我")
            simpleButton("厚乳我")
        }
        row {
            simpleButton("爆炒我")
            simpleButton("橄榄我")
            simpleButton("超市我")
        }
    } else {
        row {
            requestContactButton("点击查看+86")
            requestLocationButton("高精度Geo")
        }
        row {
            webAppButton("百度一下", "https://baidu.com/")
            simpleButton("我是傻逼")
        }
    }
}
