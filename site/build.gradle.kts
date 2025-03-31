import com.varabyte.kobweb.gradle.application.util.configAsKobwebApplication
import kotlinx.html.link
import kotlinx.html.script

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kobweb.application)
    alias(libs.plugins.kobwebx.markdown)
}

group = "com.showdow.binkes"
version = "1.0-SNAPSHOT"

kobweb {
    app {
        index {
            description.set("Powered by Kobweb")

            head.add {

                script {
                    src = "showdown.js"
                }

            }



            head.add {
                script {
                    src = "highlight.js"
                }

                link {
                    rel = "stylesheet"

                    href =   "atom-one-dark.css"
                }

            }


        }
    }
}

kotlin {
    configAsKobwebApplication("binkes", includeServer = true)
    js(IR) {
        browser {
           // webpackTask {
               // externalDependencies["showdown"] = "null" // Tell webpack to ignore
           // }
        }
    }
    sourceSets {
//        commonMain.dependencies {
//          // Add shared dependencies between JS and JVM here
//        }
        jsMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.html.core)
            implementation(libs.kobweb.core)
            implementation(libs.kobweb.silk)
            implementation(libs.silk.icons.fa)
            implementation(libs.kobwebx.markdown)
            implementation(project(":worker"))
        }
        jvmMain.dependencies {
            compileOnly(libs.kobweb.api) // Provided by Kobweb backend at runtime
        }
    }
}
