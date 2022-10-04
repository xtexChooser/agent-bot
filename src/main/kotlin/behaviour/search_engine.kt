package ml.xtexx.agentBot.behaviour

import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.extensions.utils.types.buttons.inlineKeyboard
import dev.inmo.tgbotapi.extensions.utils.types.buttons.urlButton
import dev.inmo.tgbotapi.types.message.MarkdownV2
import dev.inmo.tgbotapi.types.message.textsources.bold
import dev.inmo.tgbotapi.utils.row
import ml.xtexx.agentBot.Bot

val SEARCH_ENGINES = mapOf(
    "DDG" to "https://duckduckgo.com/",
    "必应" to "https://cn.bing.com/",
    "谷歌" to "https://www.google.com.hk/",
    "Magi" to "https://magi.com/",
    "Yandex" to "https://yandex.com/",
    "2345" to "https://www.2345.com/",
    "百度" to "https://baidu.com/",
    "F搜" to "https://fsoufsou.com/",
    "360" to "https://www.so.com/",
    "搜狗" to "https://www.sogou.com/",
    "Goobe" to "https://goobe.io/",
    "Gigablast" to "https://www.gigablast.com/",
    "头条" to "https://www.toutiao.com/",
    "Qwant" to "https://www.qwant.com/",
    "Peekier" to "https://peekier.com/",
    "中国" to "http://www.chinaso.com/",
    "有道" to "https://www.youdao.com/",
    "Yahoo!" to "https://sg.search.yahoo.com/",
    "Naver" to "https://www.naver.com/",
    "Goo" to "https://www.goo.ne.jp/",
    "Ask" to "https://www.ask.com/",
    "宜搜" to "http://www.easou.com/",
    "易查" to "http://page.yicha.cn/tp/i.y",
    "MBA智库" to "https://www.mbalib.com/",
    "秘迹" to "https://mijisou.com/",
    "Ecosia" to "https://www.ecosia.org/",
    "Startpage" to "https://www.startpage.com/",
)

suspend fun BehaviourContext.buildSearchEngine() {
    onCommand("search_engine") {
        Bot.logger.info { "Providing search engines in ${it.chat.id.chatId}" }
        reply(it, bold("搜    索    引    擎").markdownV2, MarkdownV2, replyMarkup = inlineKeyboard {
            SEARCH_ENGINES.entries
                .chunked(4)
                .forEach { batch ->
                    row {
                        batch.forEach { (name, link) ->
                            urlButton(name, link)
                        }
                    }
                }
        })
    }
}