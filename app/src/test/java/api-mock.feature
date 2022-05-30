@Ignore
Feature: Mock Downstream services


  ####################################################
  # Mock the fake cat api for demonstration purposes #
  ####################################################
  Background:
    * def uuid = function(){ return java.util.UUID.randomUUID() + '' }
    * def cats = {}

  Scenario: pathMatches('/v1/cats') && methodIs('get')
    * def response = $cats.*

  Scenario: pathMatches('/v1/cats/{id}') && methodIs('get')
    * def response = cats[pathParams.id]

  Scenario: pathMatches('/v1/cats') && methodIs('post')
    * def cat = request
    * def id = uuid()
    * cat.id = id
    * cats[id] = cat
    * def response = cat

  Scenario: pathMatches('/v1/cats/{id}') && methodIs('put')
    * def cat = request
    * cat.id = pathParams.id
    * cats[pathParams.id] = cat
    * def response = cat

  Scenario: pathMatches('/v1/cats/{id}') && methodIs('delete')
    * karate.remove('cats', '$.' + pathParams.id)
    * def response = ''

  Scenario: pathMatches('/health/full') && methodIs('get')
    * def response = ''

  ############
  # Mock IUS #
  ############
  Scenario: pathMatches('/v1/users/sign_up')
    * def response = {"user":{"userId":"1234567812345678","username":"createuser12345671234567.123_iamtestpass","namespaceId":"12345678","securityLevel":"HIGH","challengeQuestionAnswer":[{"question":"Who was your childhood hero?"}],"fullName":[{"salutation":"Mr.","givenName":"Test","middleName":"U","surName":"Intuit","suffix":"Jr"}],"displayName":["createuser12345671234567.123_iamtestpass"],"phone":[{"type":"WORK","primary":true,"phoneNumber":"+1 8005555555"}],"email":{"type":"NONE","primary":true,"address":"createuser12345671234567.123_iamtestpass@sharklasers.com"}},"iamticket":{"ticket":"V1-123-X12nabme12h1234ry1lih1","userId":"1234567812345678","userIdPseudonym":"TheUserIdPseudonym","agentId":"1234567812345678","realmId":"12345678","authenticationLevel":"30","identityAssuranceLevel":"-1","namespaceId":"12345678","access":"","scoped":false,"authTime":"9999999999999","createTime":"9999999999999","sessionId":"TheSessionId-9999999999999"}}

  Scenario: pathMatches('/v2/web/tickets/sign_in')
    * def response = {"iamTicket":{"ticket":"V1-123-X12nabme12h1234ry1lih1","userId":"1234567812345678","userIdPseudonym":"TheUserIdPseudonym","agentId":"1234567812345678","authenticationLevel":"30","identityAssuranceLevel":"-1","namespaceId":"12345678","scoped":false,"authTime":"9999999999999","createTime":"9999999999999","sessionId":"TheSessionId-9999999999999"},"action":"PASS","riskLevel":"LOW","passwordResetRequired":false}


  ##################################
  # Catch all for any missed mocks #
  ##################################
  Scenario:
    * def responseStatus = 404
    * def responseHeaders = { 'Content-Type': 'text/html; charset=utf-8' }
    * def response = <html><body>Not Found</body></html>

