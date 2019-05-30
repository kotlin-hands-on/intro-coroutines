package samples

import org.slf4j.Logger
import org.slf4j.LoggerFactory

val log: Logger = LoggerFactory.getLogger("Samples")

fun log(msg: Any?) {
    log.info(msg.toString())
}
