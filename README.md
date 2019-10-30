# Gatling-Common

A collection of classes and biolerplate used to make certain things easier in Gatling.  Intended to be used in conjunction with the (https://github.com/JohnArrowwood/gatling-sbt-seed)[gatling-sbt-seed] project.

## Publishing A New Release

NOTE TO SELF:

After updating the version number in build.sbt, then:

```
$ sbt
> clean
> commpile
> test
> publishSigned
> sonatypeBundleRelease
```
