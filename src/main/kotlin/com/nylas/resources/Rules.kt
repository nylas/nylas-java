package com.nylas.resources

import com.nylas.NylasClient
import com.nylas.models.*
import com.nylas.util.JsonHelper
import com.nylas.util.PathEncoder
import com.squareup.moshi.Types

/**
 * Nylas Rules API
 *
 * The Nylas Rules API allows you to create and manage rules for Nylas Agent Accounts.
 * Rules filter inbound mail or restrict outbound sends based on conditions and actions.
 *
 * @param client The configured Nylas API client
 */
class Rules(client: NylasClient) : Resource<Rule>(client, Rule::class.java) {
  /**
   * Return all rules for your application.
   * @param queryParams Optional query parameters to apply
   * @param overrides Optional request overrides to apply
   * @return The list of rules
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun list(queryParams: ListRulesQueryParams? = null, overrides: RequestOverrides? = null): ListResponse<Rule> {
    val nestedResponse = client.executeGet<RulesListResponse>("v3/rules", RulesListResponse::class.java, queryParams, overrides)
    return nestedResponse.toListResponse()
  }

  /**
   * Return a rule.
   * @param ruleId The ID of the rule to retrieve
   * @param overrides Optional request overrides to apply
   * @return The rule
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun find(ruleId: String, overrides: RequestOverrides? = null): Response<Rule> {
    val path = String.format("v3/rules/%s", PathEncoder.encode(ruleId))
    return findResource(path, overrides = overrides)
  }

  /**
   * Create a rule.
   * @param requestBody The values to create the rule with
   * @param overrides Optional request overrides to apply
   * @return The created rule
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun create(requestBody: CreateRuleRequest, overrides: RequestOverrides? = null): Response<Rule> {
    val serializedRequestBody = JsonHelper.moshi().adapter(CreateRuleRequest::class.java).toJson(requestBody)
    return createResource("v3/rules", serializedRequestBody, overrides = overrides)
  }

  /**
   * Update a rule.
   * @param ruleId The ID of the rule to update
   * @param requestBody The values to update the rule with
   * @param overrides Optional request overrides to apply
   * @return The updated rule
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun update(ruleId: String, requestBody: UpdateRuleRequest, overrides: RequestOverrides? = null): Response<Rule> {
    val path = String.format("v3/rules/%s", PathEncoder.encode(ruleId))
    val serializedRequestBody = JsonHelper.moshi().adapter(UpdateRuleRequest::class.java).toJson(requestBody)
    return updateResource(path, serializedRequestBody, overrides = overrides)
  }

  /**
   * Delete a rule.
   * @param ruleId The ID of the rule to delete
   * @param overrides Optional request overrides to apply
   * @return The deletion response
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun destroy(ruleId: String, overrides: RequestOverrides? = null): DeleteResponse {
    val path = String.format("v3/rules/%s", PathEncoder.encode(ruleId))
    return destroyResource(path, overrides = overrides)
  }

  /**
   * Return rule evaluation audit records for a grant.
   * @param grantId The ID of the grant to query rule evaluations for
   * @param queryParams Optional query parameters to apply
   * @param overrides Optional request overrides to apply
   * @return The list of rule evaluations
   */
  @Throws(NylasApiError::class, NylasSdkTimeoutError::class)
  @JvmOverloads
  fun listEvaluations(
    grantId: String,
    queryParams: ListRuleEvaluationsQueryParams? = null,
    overrides: RequestOverrides? = null,
  ): ListResponse<RuleEvaluation> {
    val path = String.format("v3/grants/%s/rule-evaluations", PathEncoder.encode(grantId))
    val responseType = Types.newParameterizedType(ListResponse::class.java, RuleEvaluation::class.java)
    return client.executeGet(path, responseType, queryParams, overrides)
  }
}
