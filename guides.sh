####!/bin/bash
EXIT_STATUS=0

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

#### Getting Started
./gradlew creatingYourFirstMicronautAppBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

#### Cache
./gradlew micronautCacheBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

#### Core Basics
./gradlew micronautConfigurationBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautDependencyInjectionTypesBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautScopeTypesBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Data Access
./gradlew micronautDataAccessMybatisBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautDynamodbBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


####./gradlew micronautEclipsestorePersitenceBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi


./gradlew micronautOracleAutonomousDbBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Data JDBC
./gradlew micronautCloudDatabaseAwsBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautCloudDatabaseAzureBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautCloudDatabaseGoogleBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautCloudDatabaseOracleBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautDataJdbcRepositoryBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautJavaRecordsBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Data JPA
./gradlew micronautDataHibernateReactiveBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautHibernateReactiveBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautJpaHibernateBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Data R2DBC
./gradlew micronautDataR2dbcRepositoryBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Database Modeling
./gradlew micronautDataManyToManyMysqlBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautDataManyToManyOracleBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautDataOneToManyBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Development
./gradlew addingCommitInfoBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautIntellijIdeaIdeSetupBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi




#### HTTP Client
./gradlew micronautHttpClientBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautReactorStreamingHttpClientBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### HTTP Server
./gradlew micronautContentNegotiationBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautCorsBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautHealthEndpointBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### JAXRS
./gradlew micronautJaxrsJdbcBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### JSON Schema
./gradlew micronautJsonSchemaBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi





#### Distribution
./gradlew executableJarBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautAzureCloudBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautCracGoogleCloudPlatformCloudRunBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautCreatingFirstGraalAppBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautDockerImageBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautElasticbeanstalkBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautGoogleAppEngineBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautGoogleCloudComputeBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautGraalvmNativeImageGoogleCloudPlatformCloudRunBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautOracleCloudBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Distributed Configuration
####./gradlew micronautAwsParameterStoreBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi


./gradlew micronautK8sBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautK8sAwsBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautK8sGcpBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautK8sOciBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Distributed Tracing
./gradlew micronautCloudTraceGoogleBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautCloudTraceOciBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautMicroservicesDistributedTracingJaegerBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautMicroservicesDistributedTracingJaegerOpentelemetryBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautMicroservicesDistributedTracingXrayBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautMicroservicesDistributedTracingZipkinBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautMicroservicesDistributedTracingZipkinOpentelemetryBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Email

./gradlew micronautEmailAmazonSesBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautEmailSendgridBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautOracleEmailDeliveryBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### GraalPy
./gradlew micronautGraalpyBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautGraalpyPythonPackageBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### GraalVM
./gradlew micronautCreatingFirstGraalAppBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautGraalvmNativeImageGoogleCloudPlatformCloudRunBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautGraalvmReflectionBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautServerlessFunctionAwsLambdaRequestStreamHandlerGraalvmBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew mnApplicationAwsLambdaGraalvmBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew mnServerlessFunctionAwsLambdaGraalvmBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### GraphQL
./gradlew micronautGraphqlBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautGraphqlTodoBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


#### Kotlin
./gradlew micronautKotlinExtensionFnsBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Kubernetes
./gradlew micronautK8sBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautK8sAwsBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautK8sGcpBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautK8sOciBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Logging
./gradlew micronautAzureLoggingBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautServerFilterRequestBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### MCP
####./gradlew micronautMcpDiskspaceHttpBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi
####
####
####./gradlew micronautMcpDiskspaceStdioBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi
####
####
####./gradlew micronautMcpWeatherHttpBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi
####
####
####./gradlew micronautMcpWeatherStdioBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi



#### Messaging
####./gradlew micronautJmsAwsSqsBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi


./gradlew micronautKafkaBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautMqttBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautOracleCloudStreamingBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautRabbitmqBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautRabbitmqRpcBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Metrics
./gradlew micronautMetricsBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautMetricsAwsBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautMetricsOciBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Micronaut Security
./gradlew micronautCliJwkgenBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautDatabaseAuthenticationProviderBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautSecurityApiKeyBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautSecurityBasicauthBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautSecurityJwtBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautSecurityJwtCookieBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautSecurityKeysJwksBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautSecuritySessionBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


####./gradlew micronautSecuritySessionDatabaseAuthenticationBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi


./gradlew micronautSecurityTestBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautSecurityX509Build || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautTokenPropagationBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### MongoDB
####./gradlew micronautDataMongodbAsynchronousBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi
####
####
####./gradlew micronautDataMongodbSynchronousBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi
####
####
####./gradlew micronautMongodbAsynchronousBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi
####
####
####./gradlew micronautMongodbSynchronousBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi



#### Object Storage
./gradlew micronautObjectStorageAwsBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautObjectStorageGcpBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautObjectStorageOracleCloudBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### OpenAPI
./gradlew micronautOpenapiAdocBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


####./gradlew micronautOpenapiGeneratorClientBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi
####
####
####./gradlew micronautOpenapiGeneratorServerBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi


./gradlew micronautOpenapiSwaggerUiBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Patterns
./gradlew micronautPatternsCompositeBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Registry
./gradlew micronautPushToGithubContainerRegistryBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautPushToOracleCloudContainerRegistryBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautPushToRegistryDockerHubBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Scale to Zero Containers
./gradlew micronautGoogleCloudPlatformCloudRunBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautGraalvmNativeImageGoogleCloudPlatformCloudRunBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Scheduling
./gradlew micronautAwsLambdaEventbridgeEventBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautScheduledBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Schema Migration
./gradlew micronautFlywayBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

#### Secrets Manager
./gradlew micronautAwsSecretsmanagerBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautAwsSecretsmanagerRotationBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautCloudSecretManagerGoogleBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautCloudSecretsAzureBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautCloudSecretsOracleBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Serverless
./gradlew micronautAzureHttpFunctionsBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


####./gradlew micronautGoogleCloudHttpFunctionBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi


./gradlew micronautOracleFunctionBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


####./gradlew micronautOracleFunctionHttpBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi



#### Service Discovery
./gradlew micronautK8sBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautK8sAwsBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautK8sGcpBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautK8sOciBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautMicroservicesServicesDiscoverConsulBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautMicroservicesServicesDiscoverEurekaBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi




#### Static Resources
./gradlew micronautStaticResourcesBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Testing
./gradlew micronautRestAssuredBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew replaceH2WithRealDatabaseForTestingBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew testingMicronautKafkaListenerUsingTestcontainersBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew testingRestApiIntegrationsUsingMockserverBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew workingWithJooqFlywayUsingTestcontainersBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Turbo
####./gradlew hotwireTurboMicronautChatBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi
####
####
####./gradlew micronautTurboNativeBuild || EXIT_STATUS=$?
####
####if [ $EXIT_STATUS -ne 0 ]; then
####  exit $EXIT_STATUS
####fi



#### Uncategorized

#### Validation
./gradlew micronautCustomValidationAnnotationBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautErrorHandlingBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Views
./gradlew micronautViewsThymeleafBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautWebjarsStimulusBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### WebSockets
./gradlew micronautWebsocketBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### i18n
./gradlew localizedMessageSourceBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### AWS Lambda
./gradlew micronautAwsLambdaEventbridgeEventBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautAwsLambdaS3EventBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautAwsSecretsmanagerRotationBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautServerlessFunctionAwsLambdaRequestStreamHandlerBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautServerlessFunctionAwsLambdaRequestStreamHandlerGraalvmBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew mnApplicationAwsLambdaGraalvmBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew mnApplicationAwsLambdaJavaRuntimeBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew mnServerlessFunctionAwsLambdaBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew mnServerlessFunctionAwsLambdaFunctionUrlBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew mnServerlessFunctionAwsLambdaGraalvmBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Authorization Code
./gradlew micronautCloudOidcOracleBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautOauth2Auth0Build || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautOauth2CognitoBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautOauth2GithubBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautOauth2KeycloakBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautOauth2LinkedinBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautOauth2OidcGoogleBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautOauth2OidcOciBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautOauth2OidcOciAuthenticationCookieBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautOauth2OidcStravaBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautOauth2OktaBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Beyond JSON
./gradlew micronautFileDownloadExcelBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautOpenpdfBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautProducesXmlBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Boot to Micronaut Building a REST API
./gradlew buildingARestApiSpringBootVsMicronautDataBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew buildingARestApiSpringBootVsMicronautGetListBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew buildingARestApiSpringBootVsMicronautImplemetingGetBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew buildingARestApiSpringBootVsMicronautPostBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew buildingARestApiSpringBootVsMicronautSecurityBasicAuthBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew buildingARestApiSpringBootVsMicronautTestFirstBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Client Credentials
./gradlew micronautOauth2ClientCredentialsAuth0Build || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautOauth2ClientCredentialsCognitoBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautOauth2ClientCredentialsOracleIdentityDomainBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi



#### Coordinated Restore at Checkpoint
./gradlew micronautCracGoogleCloudPlatformCloudRunBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew micronautCracHelloworldBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

#### Spring Boot
./gradlew micronautSpringBootBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew springBootMicronautDataBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew springBootToMicronautApplicationClassBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew springBootToMicronautAtConfigurationAtBeanAtFactoryBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew springBootToMicronautAtSingletonAtComponentBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi


./gradlew springBootToMicronautUriComponentsBuilderVsUriBuilderBuild || EXIT_STATUS=$?

if [ $EXIT_STATUS -ne 0 ]; then
  exit $EXIT_STATUS
fi

