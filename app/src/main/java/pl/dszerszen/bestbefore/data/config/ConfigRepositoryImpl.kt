package pl.dszerszen.bestbefore.data.config

import pl.dszerszen.bestbefore.domain.config.ConfigRepository
import pl.dszerszen.bestbefore.domain.config.model.GlobalConfig
import javax.inject.Inject

class ConfigRepositoryImpl @Inject constructor() : ConfigRepository {
    override fun getConfig(): GlobalConfig {
        return GlobalConfig()
    }
}