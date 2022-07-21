package pl.dszerszen.bestbefore.domain.config

import pl.dszerszen.bestbefore.domain.config.model.GlobalConfig

interface ConfigRepository {
    fun getConfig(): GlobalConfig
}