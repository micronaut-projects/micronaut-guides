package example.micronaut

import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName
import java.lang.InterruptedException
import java.lang.RuntimeException
import java.io.IOException

object LocalStackUtil {
    private const val TAG = "1.1.0"
    private const val IMAGE = "localstack/localstack"
    private const val DEMO_QUEUE = "demo_queue"

    private var localstack: LocalStackContainer = LocalStackContainer(DockerImageName.parse("$IMAGE:$TAG"))
        .withServices(LocalStackContainer.Service.SQS)

    init {
        localstack.start()
        setupPrerequisites()
    }

    fun getLocalStack(): LocalStackContainer {
        return localstack
    }

    private val endpoint: String
        get() = localstack.getEndpointOverride(LocalStackContainer.Service.SQS).toString()

    private fun secretAccessKey(): String {
        return localstack.secretKey
    }

    private val region: String
        get() = localstack.region

    private fun accessKeyId(): String {
        return localstack.accessKey
    }

    val properties: Map<String, String>
        get() = mapOf(
            "aws.accessKeyId" to accessKeyId(),
            "aws.secretKey" to secretAccessKey(),
            "aws.region" to region,
            "aws.sqs.endpoint-override" to endpoint
        )


    private fun setupPrerequisites() {
        try {
            localstack.execInContainer("awslocal", "sqs", "create-queue", "--queue-name", DEMO_QUEUE)
        } catch (e: InterruptedException) {
            throw RuntimeException("Failed to setup the Localstack prerequisites", e)
        } catch (e: IOException) {
            throw RuntimeException("Failed to setup the Localstack prerequisites", e)
        }
    }

    fun close() {
        localstack.close()
    }
}