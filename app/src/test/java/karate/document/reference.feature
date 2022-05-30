Feature: Reference Document Endpoint

    Background:
    * url baseUrl
    * configure headers = read('classpath:ius-headers.js')
    * def created = call read('classpath:ius-user-create.feature')
    * def user = created.user
    * def ticket = created.ticket
    * def username = created.username
    * def password = created.password
    * def args = { username : #(username), password: #(password)}
    * def signin = call read('classpath:ius-signin.feature') args
    * def ticket = signin.ticket

    Scenario: get all documents
        Given path "/v1/documents"
        When method get
        Then status 200

    Scenario: create a new document
        # Create a document
        Given path "/v1/documents"
        And header Content-Type = 'application/json; charset=utf-8'
        And request { "userName":"ktamilvanan", "content":"This is a test document" }
        When method post
        Then status 200
        And match response['content'] == 'This is a test document'
    Scenario: access health endpoint
        Given url baseUrl+"/health/full"
        When method get
        Then status 200