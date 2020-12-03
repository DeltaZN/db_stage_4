package coffee.store.controller.misc

import org.springframework.boot.web.servlet.error.DefaultErrorAttributes
import org.springframework.stereotype.Component
import org.springframework.web.context.request.WebRequest




@Component
class CustomErrorAttributes : DefaultErrorAttributes() {
    override fun getErrorAttributes(webRequest: WebRequest?, includeStackTrace: Boolean): Map<String, Any>? {
        val errorAttributes = super.getErrorAttributes(webRequest, includeStackTrace)
        errorAttributes.remove("trace")
        return errorAttributes
    }
}