# Validating the Gatling Config

When first setting up Gatling to run under SBT,
one of the first things that needed to be done was figure out
how to configure it so that it reads the data from 
`src/it/resources/data`.
This test proves that the configuration worked as expected.

There is not much need to keep the test around, but it is useful
in case one ever wanted to re-organize the directory layout.
