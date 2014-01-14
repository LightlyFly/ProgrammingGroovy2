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

