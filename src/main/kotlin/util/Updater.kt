package ml.xtexx.agentBot.util

import com.soywiz.korio.net.http.createHttpClient
import mu.KotlinLogging

object Updater {

    val logger = KotlinLogging.logger("Updater")

    suspend fun check() {
        try {
            val latest = getLatestVersion()
            logger.info { "Latest: $latest" }
            val current = getCurrentVersion()
            logger.info { "Current: $current" }
            if (current != latest) {
                logger.info { "Updating" }
                val result = update()
                logger.info { "Result: $result" }
                exit()
            }
        } catch (e: Throwable) {
            logger.error(e) { "Error occurred when checking for update" }
            e.printStackTrace()
        }
    }

    suspend fun getLatestVersion() =
        JSON.parse<dynamic>(createHttpClient().readString("https://registry.yarnpkg.com/agent-bot/latest"))
            .version

    fun getCurrentVersion() =
        JSON.parse<dynamic>(js("require(\"child_process\").spawnSync(\"npm\", [\"ls\", \"--depth\", \"0\", \"--json\", \"agent-bot\"]).stdout.toString()") as String)
            .dependencies["agent-bot"].version

    fun update() = js("require(\"child_process\").spawnSync(\"npm\", [\"install\", \"agent-bot\"])")

    fun exit(): Nothing {
        js("process.exit()")
        error("")
    }

}