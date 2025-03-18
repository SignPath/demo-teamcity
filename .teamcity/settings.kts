import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.commitStatusPublisher
import jetbrains.buildServer.configs.kotlin.triggers.vcs

version = "2024.12"

project {

  buildType {
    name = "Build and Sign"

    vcs {
      root(DslContext.settingsRoot)
      cleanCheckout = true
    }

    steps {
      // build step
      script {
        name = "Create HelloWorld.jar"
        scriptContent = 'javac src/HelloWorld.java && jar cfe HelloWorld.jar HelloWorld -C src HelloWorld.class'
        formatStderrAsError = true
      }

      // sign step
      step {
        type = "SignPathRunner"
        param("connectorUrl", "https://teamcity-connector-stable.customersimulation.int.signpath.io")
        param("organizationId", "%SignPath.OrganizationId%")
        param("apiToken", "%SignPath.ApiToken%")
        param("projectSlug", "single-jar")
        param("signingPolicySlug", "test-signing")
        //param("artifactConfigurationSlug", "teamcity")

        param("inputArtifactPath", "HelloWorld.jar")
        param("outputArtifactPath", "HelloWorld-signed.jar")
        param("waitForCompletion", true)
      }
    }

    // publish the signed artifact
    artifactRules = "HelloWorld-signed.jar"

    // max build duration 5 mins
    failureConditions {
      executionTimeoutMin = 5
    }
  }
}
