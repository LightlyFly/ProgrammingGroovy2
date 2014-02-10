class TestMethodInvocation extends GroovyTestCase {
    void testInterceptedMethodCallonPOJO() {
        def val = new Integer(3)
        Integer.metaClass.toString = { -> 'intercepted' }

        assertEquals 'intercepted', val.toString()
    }

    void testInterceptableCalled() {
        def obj = new AnInterceptable()
        assertEquals 'intercepted', obj.existingMethod()
        assertEquals 'intercepted', obj.nonExistingMethod()
    }

    void testInterceptedExistingMethodCalled() {
        AGroovyObject.metaClass.existingMethod2 = { -> 'intercepted' }
        def obj = new AGroovyObject()
        assertEquals 'intercepted', obj.existingMethod2()
    }

    void testUnInterceptedExistingMethodCalled() {
        def obj = new AGroovyObject()
        assertEquals 'existingMethod', obj.existingMethod()
    }

    void testPropertyThatIsClosureCalled() {
        def obj = new AGroovyObject()
        assertEquals 'closure called', obj.closureProp()
    }


}