package ml.xtexx.agentBot.behaviour.dn42

import com.soywiz.korio.net.createTcpClient
import com.soywiz.korio.stream.readLine
import com.soywiz.korio.stream.writeString
import com.soywiz.korio.util.isNumeric
import dev.inmo.tgbotapi.extensions.api.edit.edit
import dev.inmo.tgbotapi.extensions.api.send.reply
import dev.inmo.tgbotapi.extensions.behaviour_builder.BehaviourContext
import dev.inmo.tgbotapi.extensions.behaviour_builder.triggers_handling.onCommandWithArgs
import dev.inmo.tgbotapi.types.message.textsources.bold
import dev.inmo.tgbotapi.types.message.textsources.regular
import dev.inmo.tgbotapi.utils.*
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import ml.xtexx.agentBot.Bot

const val WHOIS_SERVER = "whois.lantian.dn42"

suspend fun BehaviourContext.buildWhois() {
    onCommandWithArgs("whois") { msg, args ->
        if (args.size != 1) {
            reply(msg, "Usage: /whois {ASN/IP/domain}")
            return@onCommandWithArgs
        }
        var target = args.first()
        if (target.length == 4 && target.all { it.isNumeric }) target = "AS424242$target"
        Bot.logger.info { "Processing WHOIS query $target in ${msg.chat.id.chatId}" }
        val reply = reply(msg, listOf(bold("Connecting to WHOIS server...")))
        launch {
            runCatching {
                val info = createTcpClient(host = WHOIS_SERVER, port = 43).run {
                    if (!connected) error("Not connected")
                    edit(reply, listOf(bold("Sending request...")))
                    writeString("$target\r\n")
                    writeString("% This is the xtex Agent Bot WHOIS query\n")
                    edit(reply, listOf(bold("Processing...")))
                    buildList {
                        add("%% WHOIS for $target %%")
                        add("%% Server: $WHOIS_SERVER %%")
                        try {
                            withTimeout(10000) {
                                while (connected && (takeLast(2).any { (it as String).isNotEmpty() } || size < 3))
                                    add(readLine())
                            }
                        } catch (e: TimeoutCancellationException) {
                            add("%% Request Timeout %%")
                        }
                        if (isEmpty()) {
                            add("%% No Data Received %%")
                        } else {
                            add("%% Total $size lines %%")
                        }
                    }
                    // @TODO: https://github.com/korlibs/korge/issues/1019
                    /*if(connected)
                        close()*/
                }

                edit(reply, buildEntities {
                    info.forEach {
                        if (it.startsWith("%%")) {
                            bold { italicln(it) }
                        } else if (it.startsWith("%")) {
                            boldln(it)
                        } else {
                            codeln(it)
                        }
                    }
                })
                Bot.logger.info { "Finished WHOIS query $target" }
            }.onFailure {
                edit(reply, listOf(bold("Error\n"), regular(it.toString())))
            }
        }
    }
}