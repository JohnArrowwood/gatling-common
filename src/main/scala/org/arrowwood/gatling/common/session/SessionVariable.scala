package org.arrowwood.gatling.common.session

/**
 * SessionVariable
 * Type used for declaring your session variable names as Scala properties
 * so you can avoid typos injecting bugs into your code, and to enable
 * tab-completion.
 */
class SessionVariable {

    /**
     * value
     * To access the value of a session variable in an Expression[String],
     * you use this syntax: "the value of foo is ${foo}"
     *
     * To use our pre-defined variables, you do it like this:
     *
     *   "The value of foo is " + FOO.value
     *
     * It is not more compact, but it gives you compile-time checking.
     */
    def value : String = "${" + this.toString + "}"

    /*
        If you store a sequence or array into the session, gatling provides
        shortcuts for accessing certain elements of the array.  The following
        provides shortcuts for that syntax attached to our variable names

        "${foo(0)}"      = FOO.first
        "${foo.size()}"  = FOO.size
        "${foo.random()} = FOO.random
    */
    def first  : String = "${" + this.toString + "(0)}"
    def size   : String = "${" + this.toString + ".size()}"
    def count  : String = "${" + this.toString + ".size()}"
    def random : String = "${" + this.toString + ".random()}"

    implicit def varname(v : SessionVariable) : String = v.toString
}
