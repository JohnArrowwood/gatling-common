# Testing the Common libraries

These tests prove the correctness of various aspects of the common libraries.

## rest/RestfulServiceSpec.scala

The tests here are all in a pending state.  They could (and maybe should)
be implemented, but the RestfulService is tested indirectly by testing
the individual services that are built on top of it.  If a major refactor is ever undertaken,
these tests should be implemented, first.

## session/IsoTimestampSpec.scala

Verifies that the value produced by the `ISO_TIMESTAMP` object .toString
method returns a string of the expected format.

Also tests that .daysAgo(), .daysFromNow() and .daysToMillis() all work
as intended.

## session/RandomGuidSpec.scala

Verifies that RandomGuid.toString() outputs a string in the expected format.


## session/SessionVariableSpec.scala

Verifies the behavior of the SessionBehavior class.

## TestSpec.scala

Verifies the default values of the various Test.<variable> values.  When
running the test, one should not set the environment variable that overrides
any of the settings, or the test will fail.
