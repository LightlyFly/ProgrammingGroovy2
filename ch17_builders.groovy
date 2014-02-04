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