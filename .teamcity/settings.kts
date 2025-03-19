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

    params {
        param("SignPath.organizationId", "C:/Sources")
        param("unix.destination.path", "/Users/Admin/Sources")
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
        param("organizationId", "%SignPath.OrganizationId%")
        param("apiToken", "credentialsJSON:17e30822-e4ce-4a6b-ad43-b85df692d573")
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
