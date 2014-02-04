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

    def placeXifStatusDone(args){
        args.length > 0 && args[0] instanceof Map &&
                args[0]['status'] == 'done' ? "x " : "- "
    }

    def printParameters(args){
        def values = ""
        if(args.length > 0 && args[0] instanceof Map) {
            values += " ["
            def count = 0
            args[0].each { key, value ->
                if( key == 'status') return
                count++
                values += (count > 1 ? " " : "")
                values += "${key}: ${value}"
            }
            values += "]"
        }

        values
    }
}

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