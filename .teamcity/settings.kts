import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.script

version = "2024.12"

project {

  buildType {
    name = "Build and Sign"
    id("build_and_sign")

    vcs {
      root(DslContext.settingsRoot)
      cleanCheckout = true
    }

    steps {
      // build step
      script {
        name = "Create HelloWorld.jar"
        scriptContent = "javac src/HelloWorld.java && jar cfe HelloWorld.jar HelloWorld -C src HelloWorld.class"
        formatStderrAsError = true
      }

      // sign step
      step {
        type = "SignPathRunner"
        param("connectorUrl", "https://teamcity-connector-stable.customersimulation.int.signpath.io")
        param("organizationId", "9ff791fc-c563-44e3-ab8c-86a33c910bbe")
        param("apiToken", "credentialsJSON:a03ec855-c92c-4f33-8877-b8ab1726afd4")
        param("projectSlug", "single-jar")
        param("signingPolicySlug", "test-signing")
        //param("artifactConfigurationSlug", "teamcity")

        param("inputArtifactPath", "HelloWorld.jar")
        param("outputArtifactPath", "HelloWorld-signed.jar")
        param("waitForCompletion", "true")
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
