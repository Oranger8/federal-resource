package my.orange.fedresurs.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("federal-resource")
class FederalResourceProperties {
    
    var url: String = ""
    var login: String = ""
    var password: String = ""
}
