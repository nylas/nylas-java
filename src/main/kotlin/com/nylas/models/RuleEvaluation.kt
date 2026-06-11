package com.nylas.models

import com.squareup.moshi.Json

/**
 * The stage where a rule evaluation happened.
 */
enum class RuleEvaluationStage {
  @Json(name = "smtp_rcpt")
  SMTP_RCPT,

  @Json(name = "inbox_processing")
  INBOX_PROCESSING,

  @Json(name = "outbound_send")
  OUTBOUND_SEND,
}

/**
 * Class representation of normalized rule evaluation input.
 */
data class RuleEvaluationInput(
  @Json(name = "from_address")
  val fromAddress: String? = null,
  @Json(name = "from_domain")
  val fromDomain: String? = null,
  @Json(name = "from_tld")
  val fromTld: String? = null,
  @Json(name = "recipient_addresses")
  val recipientAddresses: List<String>? = null,
  @Json(name = "recipient_domains")
  val recipientDomains: List<String>? = null,
  @Json(name = "recipient_tlds")
  val recipientTlds: List<String>? = null,
  @Json(name = "outbound_type")
  val outboundType: String? = null,
)

/**
 * Class representation of actions applied during a rule evaluation.
 */
data class RuleEvaluationAppliedActions(
  @Json(name = "blocked")
  val blocked: Boolean? = null,
  @Json(name = "marked_as_spam")
  val markedAsSpam: Boolean? = null,
  @Json(name = "marked_as_read")
  val markedAsRead: Boolean? = null,
  @Json(name = "marked_starred")
  val markedStarred: Boolean? = null,
  @Json(name = "archived")
  val archived: Boolean? = null,
  @Json(name = "trashed")
  val trashed: Boolean? = null,
  @Json(name = "folder_ids")
  val folderIds: List<String>? = null,
)

/**
 * Class representation of a rule evaluation audit record.
 */
data class RuleEvaluation(
  @Json(name = "id")
  val id: String? = null,
  @Json(name = "grant_id")
  val grantId: String? = null,
  @Json(name = "message_id")
  val messageId: String? = null,
  @Json(name = "evaluated_at")
  val evaluatedAt: Long? = null,
  @Json(name = "evaluation_stage")
  val evaluationStage: RuleEvaluationStage? = null,
  @Json(name = "evaluation_input")
  val evaluationInput: RuleEvaluationInput? = null,
  @Json(name = "applied_actions")
  val appliedActions: RuleEvaluationAppliedActions? = null,
  @Json(name = "matched_rule_ids")
  val matchedRuleIds: List<String>? = null,
  @Json(name = "application_id")
  val applicationId: String? = null,
  @Json(name = "organization_id")
  val organizationId: String? = null,
  @Json(name = "created_at")
  val createdAt: Long? = null,
  @Json(name = "updated_at")
  val updatedAt: Long? = null,
)
