package org.tty.dailyset.dailyset_cloud.component

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.stereotype.Component
import org.springframework.util.ResourceUtils
import org.tty.dailyset.dailyset_cloud.util.child
import java.nio.file.Files
import java.nio.file.Path
import javax.annotation.PostConstruct

@Component
class Initializer {
    @Autowired
    private lateinit var context: ConfigurableApplicationContext

    @Autowired
    private lateinit var environmentVars: EnvironmentVars

    val logger: Logger = LoggerFactory.getLogger(Initializer::class.java)

    private fun initPortraitDir() {
        /**
         * read the environment variable `dailyset.env.default_portrait`
         */
        val defaultPortraitFileName = environmentVars.defaultPortraitFileName

        /**
         * read the environment variable `dailyset.env.data.storage.portrait`
         */
        val portraitStorage = environmentVars.portraitStorage

        if (defaultPortraitFileName.isEmpty() || portraitStorage.isEmpty()) {
            logger.error("App/ Environment variable (dailyset.env.default_portrait) or (dailyset.env.data.storage.portrait) is empty.")
            return
        }

        val portraitPath = Path.of(portraitStorage)

        logger.info("App/ Portrait dir is @ " + portraitPath.toAbsolutePath())

        if (!Files.exists(portraitPath)) {
            Files.createDirectories(portraitPath)
        }

        val defaultPortraitPath = Path.of(portraitStorage, defaultPortraitFileName)


        val defaultPortraitSource = ResourceUtils.getFile("classpath:static").child(defaultPortraitFileName)
        if (!Files.exists(defaultPortraitSource.toPath())) {
            logger.error("App / Default Portrait $defaultPortraitFileName is not found.")
        }

        if (!Files.exists(defaultPortraitPath)) {
            Files.copy(defaultPortraitSource.toPath(), defaultPortraitPath)
        }

    }

    fun initialize() {
        initPortraitDir()
    }
}