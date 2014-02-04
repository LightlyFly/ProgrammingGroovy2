// literals
str = 'A string'
assert str.getClass().name == 'java.lang.String'

value = 25
println 'the value is ${value}'

str = 'hello'
assert str[2] == 'l'
try {
    str[2] = '!'
} catch (Exception ex) {
    println ex
    assert "${ex.getClass()}" == 'class groovy.lang.MissingMethodException'
}

// expressions
value = 12
assert "He paid \$${value} for that." == 'He paid $12 for that.'
assert "He paid \$$value for that." == 'He paid $12 for that.'
assert (/He paid $$value for that./) == 'He paid $12 for that.'

// replace with StringBuilder
what = new StringBuilder('fence')
text = "The cow jumped over the $what"
assert text == 'The cow jumped over the fence'

what.replace(0, 5, 'moon')      // available on a StringBuilder obj
assert text == 'The cow jumped over the moon'

// replace with GString; note it's a very different function
what = new StringBuilder('fence')
text = "The cow jumped over the $what"
assert text == 'The cow jumped over the fence'
assert text.getClass().name == 'org.codehaus.groovy.runtime.GStringImpl'
assert text.replace('o', 'x') == 'The cxw jumped xver the fence'    // GString's replace does not permanently change the variable
assert text == 'The cow jumped over the fence'

what.replace(0, 5, 'moon')      // available on a StringBuilder obj
assert text == 'The cow jumped over the moon'

val = 125

assert "The Stock closed at ${val}".getClass().name == 'org.codehaus.groovy.runtime.GStringImpl'
assert "The Stock closed at ${val}".getClass().superclass.name == 'groovy.lang.GString'
assert (/The Stock closed at ${val}/).getClass().name == 'org.codehaus.groovy.runtime.GStringImpl'
assert (/The Stock closed at ${val}/).getClass().superclass.name == 'groovy.lang.GString'
assert "This is a simple string".getClass().name == 'java.lang.String'
assert "This is a simple string".getClass().superclass.name == 'java.lang.Object'

// Laxy Eval
price = 684.71
company = 'Google'
quote = "Today $company stock closed at $price"
println quote

stocks = [Apple: 663.01, Microsoft: 30.95]

// The quote var is NOT updated although you might think it will be here.
stocks.each { key, value ->
    company = key   // merely pointing 'company' to another reference
    price = value   // merely pointing 'company' to another reference
    println quote   // still points to the original references; Google, etc.
                    // quote GString is not reevaluated.
}

companyClosure = { it.write(company) }
priceClosure = { it.write("$price") }
quote = "Today ${companyClosure} stock closed at ${priceClosure}"   // these closures cause a reevaluation to occur

stocks.each { key, value ->
    company = key
    price = value
    println quote
}

// a little more elegant
quote = "Today ${-> company} stock closed at ${-> price}"

stocks.each { key, value ->
    company = key
    price = value
    println quote
}

// multiline strings
memo = '''super
long
string
'''

message = """here is
our latest value:
\$$val"""

println message

// create XML
langs = ['C++': 'Stroustrup', 'Java': 'Gosling', 'List': 'McCarthy']
content = ''

langs.each { language, author ->
    fragment = """
        <language name="${language}">
            <author>${author}</author>
        </language>
    """

    content += fragment
}

xml = "<languages>${content}</languages>"
println xml

// convenience
str = "It's a rainy day in Seattle"

str -= "rainy "
assert str == "It's a day in Seattle"

for (str in 'held'..'helm') {
    print "${str} "
}
println ''

// RegEx
obj = ~"hello"
assert obj.getClass().name == 'java.util.regex.Pattern'

// =~ is partial RegEx match; ==~ is an exact RegEx match
pattern = ~"(G|g)roovy"
text = 'Groovy is Hip'

if (text =~ pattern) {
    assert true     // partial match
} else {
    assert false
}

if (text ==~ pattern) {
    assert false
} else {
    assert true     // not a partial match
}

matcher = 'Groovy is groovy' =~ /(G|g)roovy/
assert matcher.size() == 2
assert matcher[0][0] == 'Groovy'
assert matcher[0][1] == 'G'
assert matcher[1][0] == 'groovy'
assert matcher[1][1] == 'g'

str = 'Groovy is groovy, really groovy'
assert (str =~ /groovy/).replaceAll('hip') == 'Groovy is hip, really hip'
assert str.replaceAll( ~/groovy/, 'nasty') == 'Groovy is nasty, really nasty'
