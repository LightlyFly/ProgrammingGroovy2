class AnInterceptable implements GroovyInterceptable {
    def existingMethod() {'I will never be called because class implements GroovyInterceptable!'}

    def invokeMethod(String name, args) { 'intercepted' }   // (from GroovyObject) I override all method calls!
}

class AGroovyObject {
    def existingMethod() { 'existingMethod' }
    def existingMethod2() { 'existingMethod2' }
    def closureProp = { 'closure called' }
}

class TestMethodInvocation extends GroovyTestCase {
    void testInterceptedMethodCallonPOJO() {
        def val = new Integer(3)
        Integer.metaClass.toString = {-> 'intercepted' }

        assertEquals "intercepted", val.toString()
    }

    void testInterceptableCalled() {
        def obj = new AnInterceptable()
        assertEquals 'intercepted', obj.existingMethod()
        assertEquals 'intercepted', obj.nonExistingMethod()
    }

    void testInterceptedExistingMethodCalledPre() {     // Before the override
        def obj = new AGroovyObject()
        assertEquals 'existingMethod2', obj.existingMethod2()
    }

    void testInterceptedExistingMethodCalled() {
        AGroovyObject.metaClass.existingMethod2 = {-> 'intercepted' }   // We've just overridden existingMethod2 within this block ONLY!
        def obj = new AGroovyObject()
        assertEquals 'intercepted', obj.existingMethod2()
    }

    void testInterceptedExistingMethodCalledPost() {     // New block after the override; the override is gone.
        def obj = new AGroovyObject()
        assertEquals 'existingMethod2', obj.existingMethod2()
    }

    void testUnInterceptedExistingMethodCalled() {
        def obj = new AGroovyObject()
        assertEquals 'existingMethod', obj.existingMethod()
    }

    void testPropertyThatIsClosureCalled() {
        def obj = new AGroovyObject()
        assertEquals 'closure called', obj.closureProp()
    }

    void testMethodMissingCalledOnlyForNonExistent() {
        def obj = new ClassWithInvokeAndMissingMethod()
        assertEquals 'existingMethod', obj.existingMethod()
        assertEquals 'missing called', obj.nonExistingMethod()
    }

    void testInvokeMethodCalledForOnlyNonExistent() {
        def obj = new ClassWithInvokeOnly()
        assertEquals 'existingMethod', obj.existingMethod()
        assertEquals 'invoke called', obj.nonExistingMethod()
    }

    void testMethodFailsOnNonExistent() {
        def obj = new TestMethodInvocation()
        shouldFail (MissingMethodException) { obj.nonExistingMethod() }
    }
}

// GroovyObject
class ClassWithInvokeAndMissingMethod {
    def existingMethod() { 'existingMethod' }

    def invokeMethod(String name, args) { 'invoke called' }     // (from GroovyObject) not called since methodMissing also defined
    def methodMissing(String name, args) { 'missing called' }   // (from GroovyObject)
}

class ClassWithInvokeOnly {
    def existingMethod() { 'existingMethod' }

    def invokeMethod(String name, args) { 'invoke called' }     // (from GroovyObject)
}

// Using Meta Method 1
str = "hello"
methodName = 'toUpperCase'
methodOfInterest = str.metaClass.getMetaMethod(methodName)
assert methodOfInterest.invoke(str) == 'HELLO'

// Using Meta Method 2
print "Does String response to toUpperCase()? "
println String.metaClass.respondsTo(str, 'toUpperCase') ? 'yes' : 'no'

print "Does String response to compareTo()? "
println String.metaClass.respondsTo(str, 'compareTo') ? 'yes' : 'no'

print "Does String response to toUpperCase(int)? "
println String.metaClass.respondsTo(str, 'toUpperCase', 5) ? 'yes' : 'no'

// Accessing Object 1
def printInfo(obj) {
    usrRequestedProperty = 'bytes'
    usrRequestedMethod = 'toUpperCase'
    
    println obj[usrRequestedProperty]       // opt 1
    println obj."$usrRequestedProperty"     // opt 2

    println obj."$usrRequestedMethod"()     // opt 1
    println obj.invokeMethod(usrRequestedMethod,null)   // opt 2
}

printInfo('hello')

// Accessing Object 2
println "Properties of 'hello' are: "
'hello'.properties.each { println it }
