package vn.teko.cart.android

import android.app.Application
import android.content.Context
import vn.teko.apollo.ApolloTheme
import vn.teko.apollo.config.toApolloConfiguration
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader

class MyApplication : Application() {

    companion object {
        const val APP_NAME = "Test CartSDK"
        const val DISCOVERY_PRODUCTS_TEST = "DiscoveryProducts_test.json"

        fun getAssetFileContent(
            context: Context,
            file: String
        ): String {
            var content = ""
            var line: String?
            val br = BufferedReader(
                InputStreamReader(
                    context.assets.open(file),
                    "UTF-8"
                ) as Reader
            )
            line = br.readLine()
            while (line != null) {
                content += line
                line = br.readLine()
            }

            br.close()
            return content.replace("\n", "")
        }

        fun initApolloInstance(config: String, appName: String): ApolloTheme {
            val apolloConfig = config.toApolloConfiguration()

            return ApolloTheme.createInstance(
                appName,
                apolloConfig.data.apolloColorTheme
            )
        }
    }
}