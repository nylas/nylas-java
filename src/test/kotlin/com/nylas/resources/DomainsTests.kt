package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.models.Response
import com.nylas.util.JsonHelper
import com.squareup.moshi.Types
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.mockito.Mockito
import org.mockito.kotlin.*
import java.io.ByteArrayInputStream
import java.lang.reflect.Type
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.Signature
import java.util.Base64
import java.util.concurrent.atomic.AtomicReference
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DomainsTests {

  @Nested
  inner class SerializationTests {
    @Test
    fun `SendTransactionalEmailRequest minimal payload serializes correctly`() {
      val adapter = JsonHelper.moshi().adapter(SendTransactionalEmailRequest::class.java)
      val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(email = "jane.doe@example.com", name = "Jane Doe")),
        from = EmailName(email = "support@acme.com", name = "ACME Support"),
      ).build()

      val json = adapter.toJson(request)

      // from must be a single object, not an array
      assert(json.contains("\"from\":{")) { "Expected 'from' to be a single object, got: $json" }
      assert(json.contains("\"to\":[")) { "Expected 'to' to be an array, got: $json" }
      assert(!json.contains("null")) { "Expected no null fields in minimal payload, got: $json" }
    }

    @Test
    fun `SendTransactionalEmailRequest full payload round-trip serializes correctly`() {
      val adapter = JsonHelper.moshi().adapter(SendTransactionalEmailRequest::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
        {
          "to": [{"name": "Jane Doe", "email": "jane.doe@example.com"}],
          "from": {"name": "ACME Support", "email": "support@acme.com"},
          "cc": [{"name": "CC User", "email": "cc@example.com"}],
          "bcc": [{"name": "BCC User", "email": "bcc@example.com"}],
          "reply_to": [{"name": "Reply", "email": "reply@example.com"}],
          "subject": "Welcome to ACME",
          "body": "Welcome! We're here to help.",
          "send_at": 1620000000,
          "reply_to_message_id": "msg-123",
          "tracking_options": {"opens": true, "links": true, "thread_replies": false, "label": "welcome"},
          "use_draft": false,
          "custom_headers": [{"name": "X-Custom", "value": "custom-value"}],
          "is_plaintext": false
        }
        """.trimIndent(),
      )

      val request = adapter.fromJson(jsonBuffer)!!
      assertIs<SendTransactionalEmailRequest>(request)
      assertEquals(1, request.to.size)
      assertEquals("jane.doe@example.com", request.to[0].email)
      assertEquals("support@acme.com", request.from.email)
      assertEquals("ACME Support", request.from.name)
      assertEquals(1, request.cc?.size)
      assertEquals("cc@example.com", request.cc?.get(0)?.email)
      assertEquals(1, request.bcc?.size)
      assertEquals("bcc@example.com", request.bcc?.get(0)?.email)
      assertEquals(1, request.replyTo?.size)
      assertEquals("reply@example.com", request.replyTo?.get(0)?.email)
      assertEquals("Welcome to ACME", request.subject)
      assertEquals("Welcome! We're here to help.", request.body)
      assertEquals(1620000000L, request.sendAt)
      assertEquals("msg-123", request.replyToMessageId)
      assertEquals(true, request.trackingOptions?.opens)
      assertEquals(true, request.trackingOptions?.links)
      assertEquals(false, request.trackingOptions?.threadReplies)
      assertEquals("welcome", request.trackingOptions?.label)
      assertEquals(false, request.useDraft)
      assertEquals(1, request.customHeaders?.size)
      assertEquals("X-Custom", request.customHeaders?.get(0)?.name)
      assertEquals("custom-value", request.customHeaders?.get(0)?.value)
      assertEquals(false, request.isPlaintext)
    }

    @Test
    fun `SendTransactionalEmailRequest isPlaintext true serializes correctly`() {
      val adapter = JsonHelper.moshi().adapter(SendTransactionalEmailRequest::class.java)
      val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(email = "jane.doe@example.com", name = "Jane Doe")),
        from = EmailName(email = "support@acme.com", name = "ACME Support"),
      ).isPlaintext(true).build()

      val json = adapter.toJson(request)
      assert(json.contains("\"is_plaintext\":true")) { "Expected is_plaintext:true in JSON, got: $json" }
    }

    @Test
    fun `SendTransactionalEmailRequest null optionals are omitted from JSON`() {
      val adapter = JsonHelper.moshi().adapter(SendTransactionalEmailRequest::class.java)
      val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(email = "jane.doe@example.com", name = "Jane Doe")),
        from = EmailName(email = "support@acme.com", name = "ACME Support"),
      ).build()

      val json = adapter.toJson(request)
      assert(!json.contains("\"cc\"")) { "Expected no cc field, got: $json" }
      assert(!json.contains("\"bcc\"")) { "Expected no bcc field, got: $json" }
      assert(!json.contains("\"subject\"")) { "Expected no subject field, got: $json" }
      assert(!json.contains("\"body\"")) { "Expected no body field, got: $json" }
      assert(!json.contains("\"send_at\"")) { "Expected no send_at field, got: $json" }
      assert(!json.contains("\"is_plaintext\"")) { "Expected no is_plaintext field, got: $json" }
    }

    @Test
    fun `Domain ignores unexpected response fields`() {
      val adapter = JsonHelper.moshi().adapter(Domain::class.java)
      val jsonBuffer = Buffer().writeUtf8(
        """
        {
          "id": "dom-123",
          "name": "Acme",
          "domain_address": "mail.acme.com",
          "organization_id": "org-123",
          "region": "us",
          "unexpected_response_id": "unexpected-value",
          "unexpected_status": "internal-value",
          "unexpected_timestamp": 1742933005,
          "verified_ownership": true,
          "verified_dkim": true,
          "verified_spf": true,
          "verified_mx": true,
          "verified_feedback": false,
          "verified_dmarc": false,
          "verified_arc": false,
          "created_at": 1742932766,
          "updated_at": 1742933005
        }
        """.trimIndent(),
      )

      val domain = adapter.fromJson(jsonBuffer)!!
      val json = adapter.toJson(domain)

      assertEquals("dom-123", domain.id)
      assertEquals("mail.acme.com", domain.domainAddress)
      assert(!json.contains("unexpected_response_id")) {
        "Expected unexpected_response_id to stay out of public serialization, got: $json"
      }
      assert(!json.contains("unexpected_status")) {
        "Expected unexpected_status to stay out of public serialization, got: $json"
      }
      assert(!json.contains("unexpected_timestamp")) {
        "Expected unexpected_timestamp to stay out of public serialization, got: $json"
      }
    }

    @Test
    fun `UpdateDomainRequest only serializes documented update fields`() {
      val adapter = JsonHelper.moshi().adapter(UpdateDomainRequest::class.java)
      val request = UpdateDomainRequest.Builder().name("Renamed").build()

      val json = adapter.toJson(request)

      assertEquals("""{"name":"Renamed"}""", json)
    }

    @Test
    fun `ListDomainsQueryParams only exposes documented query parameters`() {
      val queryParams = ListDomainsQueryParams.Builder()
        .limit(5)
        .pageToken("cursor123")
        .domain("mail.acme.com")
        .region(Region.US)
        .build()

      assertEquals(
        mapOf("limit" to 5.0, "page_token" to "cursor123", "domain" to "mail.acme.com", "region" to "us"),
        queryParams.convertToMap(),
      )
    }

    @Test
    fun `DomainVerificationRequest supports all public verification request types`() {
      val adapter = JsonHelper.moshi().adapter(DomainVerificationRequest::class.java)

      assertEquals("""{"type":"dmarc"}""", adapter.toJson(DomainVerificationRequest(DomainVerificationRequestType.DMARC)))
      assertEquals("""{"type":"arc"}""", adapter.toJson(DomainVerificationRequest(DomainVerificationRequestType.ARC)))
    }
  }

  @Nested
  inner class CrudTests {
    private lateinit var domainName: String
    private lateinit var mockNylasClient: NylasClient
    private lateinit var domains: Domains

    private fun serviceAccountOverrides() = RequestOverrides(
      headers = mapOf(
        "X-Nylas-Kid" to "service-account-id",
        "X-Nylas-Timestamp" to "1742932766",
        "X-Nylas-Nonce" to "nonce",
        "X-Nylas-Signature" to "signature",
      ),
    )

    @BeforeEach
    fun setup() {
      domainName = "acme.com"
      mockNylasClient = Mockito.mock(NylasClient::class.java)
      domains = Domains(mockNylasClient)
    }

    @Test
    fun `sending a transactional email calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(SendTransactionalEmailRequest::class.java)
      val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(email = "jane.doe@example.com", name = "Jane Doe")),
        from = EmailName(email = "support@acme.com", name = "ACME Support"),
      )
        .subject("Welcome to ACME")
        .body("Welcome! We're here to help.")
        .build()

      domains.sendTransactionalEmail(domainName, request)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<Message>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/domains/$domainName/messages/send", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Message::class.java), typeCaptor.firstValue)
      assertEquals(adapter.toJson(request), requestBodyCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
    }

    @Test
    fun `sending a transactional email with overrides passes them through`() {
      val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(email = "jane.doe@example.com", name = "Jane Doe")),
        from = EmailName(email = "support@acme.com", name = "ACME Support"),
      ).build()
      val overrides = RequestOverrides(apiKey = "override-key")

      domains.sendTransactionalEmail(domainName, request, overrides)

      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<Message>>(
        any(),
        any(),
        any(),
        anyOrNull(),
        overrideParamCaptor.capture(),
      )
      assertEquals("override-key", overrideParamCaptor.firstValue.apiKey)
    }

    @Test
    fun `sending a transactional email with a large attachment calls requests with the correct params`() {
      val adapter = JsonHelper.moshi().adapter(SendTransactionalEmailRequest::class.java)
      val testInputStream = ByteArrayInputStream("test data".toByteArray())
      val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(email = "jane.doe@example.com", name = "Jane Doe")),
        from = EmailName(email = "support@acme.com", name = "ACME Support"),
      )
        .subject("Welcome to ACME")
        .attachments(
          listOf(
            CreateAttachmentRequest(
              content = testInputStream,
              contentType = "text/plain",
              filename = "attachment.txt",
              size = 3 * 1024 * 1024,
            ),
          ),
        )
        .build()
      val attachmentLessRequest = request.copy(attachments = null)

      domains.sendTransactionalEmail(domainName, request)

      val pathCaptor = argumentCaptor<String>()
      val methodCaptor = argumentCaptor<NylasClient.HttpMethod>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<RequestBody>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeFormRequest<Response<Message>>(
        pathCaptor.capture(),
        methodCaptor.capture(),
        requestBodyCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/domains/$domainName/messages/send", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Message::class.java), typeCaptor.firstValue)
      assertEquals(NylasClient.HttpMethod.POST, methodCaptor.firstValue)
      assertNull(queryParamCaptor.firstValue)
      val multipart = requestBodyCaptor.firstValue as MultipartBody
      assertEquals(2, multipart.size)
      val buffer = Buffer()
      val fileBuffer = Buffer()
      multipart.part(0).body.writeTo(buffer)
      multipart.part(1).body.writeTo(fileBuffer)
      assertEquals(adapter.toJson(attachmentLessRequest), buffer.readUtf8())
      assertEquals("test data", fileBuffer.readUtf8())
    }

    @Test
    fun `sending a transactional email with a large attachment and overrides passes them through`() {
      val testInputStream = ByteArrayInputStream("test data".toByteArray())
      val request = SendTransactionalEmailRequest.Builder(
        to = listOf(EmailName(email = "jane.doe@example.com", name = "Jane Doe")),
        from = EmailName(email = "support@acme.com", name = "ACME Support"),
      )
        .attachments(
          listOf(
            CreateAttachmentRequest(
              content = testInputStream,
              contentType = "text/plain",
              filename = "attachment.txt",
              size = 3 * 1024 * 1024,
            ),
          ),
        )
        .build()
      val overrides = RequestOverrides(apiKey = "override-key")

      domains.sendTransactionalEmail(domainName, request, overrides)

      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeFormRequest<Response<Message>>(
        any(),
        any(),
        any(),
        any(),
        anyOrNull(),
        overrideParamCaptor.capture(),
      )
      assertEquals("override-key", overrideParamCaptor.firstValue.apiKey)
    }

    @Test
    fun `listing managed domains calls requests with the correct params`() {
      val queryParams = ListDomainsQueryParams(limit = 5, pageToken = "cursor123", domain = "mail.acme.com", region = Region.US)
      val overrides = serviceAccountOverrides()
      domains.list(queryParams, overrides)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<ListResponse<Domain>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/admin/domains", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(ListResponse::class.java, Domain::class.java), typeCaptor.firstValue)
      assertEquals(queryParams, queryParamCaptor.firstValue)
      assertServiceAccountOverrides(overrides, overrideParamCaptor.firstValue)
    }

    @Test
    fun `managed domain endpoints require service account signing headers`() {
      val exception = assertFailsWith<IllegalArgumentException> {
        domains.list(ListDomainsQueryParams(limit = 5), RequestOverrides(headers = mapOf("X-Nylas-Kid" to "")))
      }

      assert(exception.message!!.contains("X-Nylas-Timestamp")) {
        "Expected missing signing headers in error, got: ${exception.message}"
      }
    }

    @Test
    fun `managed domain list passes service account signing headers through request overrides`() {
      val capturedRequest = AtomicReference<Request>()
      val httpClientBuilder = OkHttpClient.Builder().addInterceptor { chain ->
        val request = chain.request()
        capturedRequest.set(request)
        okhttp3.Response.Builder()
          .request(request)
          .protocol(Protocol.HTTP_1_1)
          .code(200)
          .message("OK")
          .body("""{"data":[],"request_id":"req-123"}""".toResponseBody("application/json".toMediaType()))
          .build()
      }
      val client = NylasClient("api-key", httpClientBuilder, "https://api.test.nylas.com/")
      val overrides = serviceAccountOverrides()

      client.domains().list(overrides)

      val request = capturedRequest.get()
      assertNull(request.header("Authorization"))
      assertEquals("service-account-id", request.header("X-Nylas-Kid"))
      assertEquals("1742932766", request.header("X-Nylas-Timestamp"))
      assertEquals("nonce", request.header("X-Nylas-Nonce"))
      assertEquals("signature", request.header("X-Nylas-Signature"))
    }

    @Test
    fun `non-domain requests keep bearer authorization`() {
      val capturedRequest = AtomicReference<Request>()
      val httpClientBuilder = OkHttpClient.Builder().addInterceptor { chain ->
        val request = chain.request()
        capturedRequest.set(request)
        okhttp3.Response.Builder()
          .request(request)
          .protocol(Protocol.HTTP_1_1)
          .code(200)
          .message("OK")
          .body("""{"data":[],"request_id":"req-123"}""".toResponseBody("application/json".toMediaType()))
          .build()
      }
      val client = NylasClient("api-key", httpClientBuilder, "https://api.test.nylas.com/")

      client.grants().list()

      assertEquals("Bearer api-key", capturedRequest.get().header("Authorization"))
    }

    @Test
    fun `creating managed domain with signer sends canonical body and generated signing headers`() {
      val keyPair = testKeyPair()
      val capturedRequest = AtomicReference<Request>()
      val capturedBody = AtomicReference<String>()
      val httpClientBuilder = OkHttpClient.Builder().addInterceptor { chain ->
        val request = chain.request()
        val buffer = Buffer()
        request.body!!.writeTo(buffer)
        capturedRequest.set(request)
        capturedBody.set(buffer.readUtf8())
        okhttp3.Response.Builder()
          .request(request)
          .protocol(Protocol.HTTP_1_1)
          .code(200)
          .message("OK")
          .body("""{"data":{"id":"dom-123"},"request_id":"req-123"}""".toResponseBody("application/json".toMediaType()))
          .build()
      }
      val client = NylasClient("api-key", httpClientBuilder, "https://api.test.nylas.com/")
      val signer = ServiceAccountSigner(keyPair.private, "service-account-key-id")

      client.domains().create(
        CreateDomainRequest(name = "Acme", domainAddress = "mail.acme.com"),
        signer,
        RequestOverrides(headers = mapOf("X-Custom" to "keep")),
      )

      val request = capturedRequest.get()
      val body = capturedBody.get()
      assertEquals("""{"domain_address":"mail.acme.com","name":"Acme"}""", body)
      assertNull(request.header("Authorization"))
      assertEquals("keep", request.header("X-Custom"))
      assertEquals("service-account-key-id", request.header("X-Nylas-Kid"))
      assertTrue(request.header("X-Nylas-Nonce")!!.isNotBlank())
      assertTrue(request.header("X-Nylas-Timestamp")!!.isNotBlank())
      assertTrue(
        verifyServiceAccountSignature(keyPair, request, body),
        "Expected generated service account signature to verify against canonical request data",
      )
    }

    @Test
    fun `finding a managed domain calls requests with the correct params`() {
      val overrides = serviceAccountOverrides()
      domains.find("domain/with/slash", overrides)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeGet<Response<Domain>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/admin/domains/domain%2Fwith%2Fslash", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Domain::class.java), typeCaptor.firstValue)
      assertServiceAccountOverrides(overrides, overrideParamCaptor.firstValue)
    }

    @Test
    fun `creating a managed domain calls requests with the correct params`() {
      val requestBody = CreateDomainRequest(name = "Acme", domainAddress = "mail.acme.com")
      val overrides = serviceAccountOverrides()
      domains.create(requestBody, overrides)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<Domain>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/admin/domains", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Domain::class.java), typeCaptor.firstValue)
      assertEquals("""{"domain_address":"mail.acme.com","name":"Acme"}""", requestBodyCaptor.firstValue)
      assertServiceAccountOverrides(overrides, overrideParamCaptor.firstValue)
    }

    @Test
    fun `updating a managed domain calls requests with the correct params`() {
      val requestBody = UpdateDomainRequest(name = "Renamed")
      val overrides = serviceAccountOverrides()
      domains.update("dom-123", requestBody, overrides)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePut<Response<Domain>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/admin/domains/dom-123", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, Domain::class.java), typeCaptor.firstValue)
      assertEquals("""{"name":"Renamed"}""", requestBodyCaptor.firstValue)
      assertServiceAccountOverrides(overrides, overrideParamCaptor.firstValue)
    }

    @Test
    fun `destroying a managed domain calls requests with the correct params`() {
      val overrides = serviceAccountOverrides()
      domains.destroy("dom-123", overrides)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executeDelete<DeleteResponse>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/admin/domains/dom-123", pathCaptor.firstValue)
      assertEquals(DeleteResponse::class.java, typeCaptor.firstValue)
      assertServiceAccountOverrides(overrides, overrideParamCaptor.firstValue)
    }

    @Test
    fun `getting managed domain info calls requests with the correct params`() {
      val requestBody = DomainVerificationRequest(DomainVerificationRequestType.SPF)
      val overrides = serviceAccountOverrides()
      domains.info("dom-123", requestBody, overrides)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<DomainVerificationResult>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/admin/domains/dom-123/info", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, DomainVerificationResult::class.java), typeCaptor.firstValue)
      assertEquals("""{"type":"spf"}""", requestBodyCaptor.firstValue)
      assertServiceAccountOverrides(overrides, overrideParamCaptor.firstValue)
    }

    @Test
    fun `verifying a managed domain calls requests with the correct params`() {
      val requestBody = DomainVerificationRequest(DomainVerificationRequestType.DKIM)
      val overrides = serviceAccountOverrides()
      domains.verify("dom-123", requestBody, overrides)

      val pathCaptor = argumentCaptor<String>()
      val typeCaptor = argumentCaptor<Type>()
      val requestBodyCaptor = argumentCaptor<String>()
      val queryParamCaptor = argumentCaptor<IQueryParams>()
      val overrideParamCaptor = argumentCaptor<RequestOverrides>()
      verify(mockNylasClient).executePost<Response<DomainVerificationResult>>(
        pathCaptor.capture(),
        typeCaptor.capture(),
        requestBodyCaptor.capture(),
        queryParamCaptor.capture(),
        overrideParamCaptor.capture(),
      )

      assertEquals("v3/admin/domains/dom-123/verify", pathCaptor.firstValue)
      assertEquals(Types.newParameterizedType(Response::class.java, DomainVerificationResult::class.java), typeCaptor.firstValue)
      assertEquals("""{"type":"dkim"}""", requestBodyCaptor.firstValue)
      assertServiceAccountOverrides(overrides, overrideParamCaptor.firstValue)
    }

    private fun assertServiceAccountOverrides(expected: RequestOverrides, actual: RequestOverrides) {
      assertEquals(expected.apiKey, actual.apiKey)
      assertEquals(expected.apiUri, actual.apiUri)
      assertEquals(expected.timeout, actual.timeout)
      assertEquals(expected.headers, actual.headers)
      assertTrue(actual.omitAuthorization)
    }

    private fun verifyServiceAccountSignature(keyPair: KeyPair, request: Request, body: String?): Boolean {
      val envelope = linkedMapOf<String, Any>(
        "method" to request.method.lowercase(),
        "nonce" to request.header("X-Nylas-Nonce")!!,
        "path" to request.url.encodedPath,
        "timestamp" to request.header("X-Nylas-Timestamp")!!.toLong(),
      )
      if (body != null) {
        envelope["payload"] = body
      }
      val verifier = Signature.getInstance("SHA256withRSA")
      verifier.initVerify(keyPair.public)
      verifier.update(ServiceAccountSigner.canonicalJson(envelope).toByteArray(Charsets.UTF_8))
      return verifier.verify(Base64.getDecoder().decode(request.header("X-Nylas-Signature")))
    }

    private fun testKeyPair(): KeyPair {
      val generator = KeyPairGenerator.getInstance("RSA")
      generator.initialize(2048)
      return generator.generateKeyPair()
    }
  }
}
