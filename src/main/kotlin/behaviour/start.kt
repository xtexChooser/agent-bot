package ml.xtexx.agentBot.behaviour

import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.api.send.send
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommand
import dev.inmo.tgbotapi.types.message.Markdown
import dev.inmo.tgbotapi.types.message.MarkdownV2
import ml.xtexx.agentBot.Bot

const val START_REPLY = """
**Hello\!**

This is the xtex's Agent Bot, a bot made by @xtexChooser\.

**What can me do?**

1\. Private messages will be forwarded to xtex\. However, PM me directly is better\. **No replies will appear in this bot**
2\. Exercise admin rights in some groups or channels managed by xtex\.

**Something wrong?**

Contact @xtexChooser\. Other contacts are available on [my website](https://xtexx.ml/blog/about/contact)\.
"""

suspend fun BehaviourContext.buildStart() {
    onCommand("start") {
        Bot.logger.info { "Reply start command in ${it.chat.id.chatId}" }
        send(it.chat, START_REPLY, MarkdownV2)
    }
}