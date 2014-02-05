import groovy.json.JsonSlurper

bldr = new groovy.xml.MarkupBuilder()

bldr.languages{
    language(name:'C++') { author('Stroustrup'); test('hi') }
    language(name:'Java') { author('Gosling') }
    language(name:'Lisp') { author('McCarthy') }
}

println ''

langs = ['C++': 'Stroustrup', 'Java': 'Gosling', 'Lisp': 'McCarthy']

writer = new StringWriter()
bldr = new groovy.xml.MarkupBuilder(writer)
bldr.languages {
    langs.each { key, value ->
        language(name: key) {
            author(value)
        }
    }
}
println writer

println ''

xmlDocument = new groovy.xml.StreamingMarkupBuilder().bind() {
    mkp.xmlDeclaration()    // 'mkp' is the builder-support property
    mkp.declareNamespace(computer: "Computer")
    languages {
        comment << "Created using StreamingMarkupBuilder"
        langs.each { key, value ->
            language(name: key) {
                author(value)
            }
        }
    }
}

println xmlDocument

class Person3 {
    String first
    String last
    def sigs
    def tools
}

john = new Person3(first: "John", last: "Smith", sigs: ['Java', 'Groovy'], tools: ['script': 'Groovy', 'test': 'Spock'])
bldr = new groovy.json.JsonBuilder(john)
writer = new StringWriter()
bldr.writeTo(writer)
println writer

bldr = new groovy.json.JsonBuilder()
bldr {
    firstName john.first
    lastName john.last
    "special interest groups" john.sigs
    "preferred tools"{
        numberOfTools john.tools.size()
        tools john.tools
    }
}
writer = new StringWriter()
bldr.writeTo(writer)
println writer

def slurper = new JsonSlurper()
def person = slurper.parse( new FileReader('person.json'))
assert person.getClass().name == 'java.util.HashMap'
println "$person.first $person.last is interested in ${person.sigs.join(', ')}"

// DSL = Domain-Specific Language
bldr = new TodoBuilder()

bldr.build {
    Prepare_Vacation (start: '02/15', end: '02/22'){
        Reserve_Flight (on:'01/01', status: 'done')
        Reserve_Hotel(on: '01/02')
        Reserve_Car(on: '01/02')
    }
    Buy_New_Mac{
        Install_QuickSilver
        Install_TextMate
        Install_Groovy {
            Run_all_tests
        }
    }
}

class TodoBuilder {
    def level = 0
    def result = new StringWriter()

    def build(closure) {
        result << "To-Do:\n"
        closure.delegate = this
        closure()
        println result
    }

    def methodMissing(String name, args) {
        handle(name, args)
    }

    def propertyMissing(String name) {
        Object[] emptyArray = []
        handle(name, emptyArray)
    }

    def handle(String name, args) {
        level++
        level.times {
            result << " "
        }
        result << placeXifStatusDone(args)
        result << name.replaceAll("_", " ")
        result << printParameters(args)
        result << "\n"
        
        if(args.length > 0 && args[-1] instanceof Closure){
            def theClosure = args[-1]
            theClosure.delegate = this
            theClosure()
        }

        level--
    }

    static def placeXifStatusDone(args){
        args.length > 0 && args[0] instanceof Map &&
                args[0]['status'] == 'done' ? "x " : "- "
    }

    static def printParameters(args){
        def values = ""
        if(args.length > 0 && args[0] instanceof Map) {
            values += " ["
            def count = 0
            args[0].each { key, value ->
                if( key == 'status') return     // special handling for 'status' key
                count++
                values += (count > 1 ? " " : "")
                values += "${key}: ${value}"
            }
            values += "]"
        }

        values
    }
}

bldr = new TodoBuilderWithSupport()

bldr.build {
    Prepare_Vacation (start: '02/15', end: '02/22'){
        Reserve_Flight (on:'01/01', status: 'done')
        Reserve_Hotel(on: '01/02')
        Reserve_Car(on: '01/02')
    }
    Buy_New_Mac{
        Install_QuickSilver
        Install_TextMate
        Install_Groovy {
            Run_all_tests
        }
    }
}


class TodoBuilderWithSupport extends BuilderSupport {
    int level = 0
    def result = new StringWriter()

    void setParent(parent, child) {}

    def createNode(name) {
        if (name == 'build') {
            result << 'To-Do:\n:'
            'buildnode'
        } else {
            handle(name, [:])
        }
    }

    def createNode(name, Object value) {
        throw new Exception("Invalid format")
    }

    def createNode(name, Map attribute) {
        handle(name, attribute)
    }

    def createNode(name, Map attribute, Object value) {
        throw new Exception("Invalid format")
    }

    def propertyMissing(String name) {
        handle(name, [:])
        level--
    }

    void nodeCompleted(parent, node) {
        level--
        if (node == 'buildnode') {
            println result
        }
    }

    def handle(String name, attributes) {
        level++
        level.times { result << " " }
        result << placeXifStatusDone(attributes)
        result << name.replaceAll("_", " ")
        result << printParameters(attributes)
        result << "\n"
        name
    }

    def placeXifStatusDone(attributes) {
        attributes['status'] == 'done' ? 'x ' : '- '
    }

    def printParameters(attributes) {
        def values = ""
        if (attributes.size() > 0) {
            values += " ["
            def count = 0
            attributes.each { key, value ->
                if (key == 'status') return
                count++
                values += (count > 1 ? " " : "")
                values += "${key}: ${value}"
            }
            values += "]"
        }
        values
    }
}


// RobotBuilder
def blder = new RobotBuilder()

def robot = blder.robot('iRobot') {
    forward(dist: 20)
    left(rotation: 90)
    forward(speed: 10, duration: 5)
}

robot.go()

class RobotBuilder extends FactoryBuilderSupport {
    {
        registerFactory( 'robot', new RobotFactory() )
        registerFactory( 'forward', new ForwardMoveFactory() )
        registerFactory( 'left', new LeftTurnFactory() )
    };
}

class Robot3 {
    String name
    def movements = []

    void go() {
        println "Robot3 $name operating..."
        movements.each {
            movement -> println movement
        }
    }
}

class FordwardMove {
    def dist

    String toString() {
        "move distance... $dist"
    }
}

class LeftTurn {
    def rotation

    String toString() {
        "turn left... $rotation degress"
    }
}

class RobotFactory extends AbstractFactory {
    def newInstance(FactoryBuilderSupport builder, name, value, Map attributes) {
        new Robot3(name:value)
    }

    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        parent.movements << child
    }
}

class ForwardMoveFactory extends AbstractFactory {
    boolean isLead() {
        true
    }

    def newInstance(FactoryBuilderSupport builder, name, value, Map attributes) {
        new FordwardMove()
    }

    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        if (attributes.speed && attributes.duration) {
            node.dist = attributes.speed * attributes.duration
            attributes.remove('speed')
            attributes.remove('duration')
        }
        true
    }
}

class LeftTurnFactory extends AbstractFactory {
    boolean isLeaf() {
        true
    }

    def newInstance(FactoryBuilderSupport builder, name, value, Map attributes) {
        new LeftTurn()
    }
}

def robotBldr = new RobotBuilder()
robotBldr.robot('bRobot') {
    forward(dist:20){}
}
