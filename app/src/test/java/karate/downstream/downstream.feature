@ignore
Feature: Downstream dependency example

  Background:
    * url downstreamApiUrlBase

    # Don't run this feature if we're not running the mock server. This cat api doesn't actually exist.
    * if (env != 'mock') karate.abort()

  Scenario: get all the cats
    Given path 'v1', 'cats'
    When method get
    Then status 200
    * print response

  Scenario: create a new cat
    # Create a cat
    Given path 'v1', 'cats'
    And header Content-Type = 'application/json; charset=utf-8'
    And request { name: "Mr. Bigglesworth", content: "Mr. Bigglesworth is Dr. Evil's hairless pet cat." }
    When method post
    Then status 200
    * print response
    And match response['content'] == "Mr. Bigglesworth is Dr. Evil's hairless pet cat."
    * def id = response['id']

    # Make sure the created cat exists with the same content
    Given path 'v1', 'cats'
    And path id
    When method get
    Then status 200
    * print response
    And def cat = response
    And match cat['content'] == "Mr. Bigglesworth is Dr. Evil's hairless pet cat."

    # Update the cat with new content
    Given path 'v1', 'cats'
    And path id
    And request { name: "Mr. Bigglesworth", content: "Mr. Bigglesworth was forced into a hasty escape in a cryogenic capsule causing all of his hair to fall out during the process and rendering him permanently bald." }
    When method put
    Then status 200
    * print response
    And def cat = response
    And match cat['content'] == "Mr. Bigglesworth was forced into a hasty escape in a cryogenic capsule causing all of his hair to fall out during the process and rendering him permanently bald."

    # Delete the created cat
    Given path 'v1', 'cats'
    And path id
    When method delete
    Then status 200

    # Check for the deleted cat
    Given path 'v1', 'cats'
    And path id
    * print response
    When method get
    Then status 200
    And match response !contains { id: '#(id)', name: '#string', content: '#string' }