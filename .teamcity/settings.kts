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
      signPathSubmitSigningRequest {
        connectorUrl = "https://teamcity-connector-stable.customersimulation.int.signpath.io"     
        organizationId = "%SignPath.OrganizationId%"
        apiToken = "credentialsJSON:a03ec855-c92c-4f33-8877-b8ab1726afd4"
        projectSlug = "single-jar"
        signingPolicySlug = "test-signing"
        inputArtifactPath = "HelloWorld.jar"
        outputArtifactPath = "HelloWorld-signed.jar"
        waitForCompletion = true
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
