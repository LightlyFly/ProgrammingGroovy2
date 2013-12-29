/**
 * Created by epierce on 12/16/13.
 */

// Lists
def lst = [1,3,4,1,8,9,2,6]
assert lst == [1,3,4,1,8,9,2,6]
assert lst.getClass().name == 'java.util.ArrayList'

assert lst[0] == 1
assert lst[lst.size() - 1] == 6

assert lst[-1] == 6
assert lst[-2] == 2

assert lst[2..5] == [4,1,8,9]
assert lst[-6..-3] == [4, 1, 8, 9]

subLst = lst[2..5]
println subLst.dump()
subLst[0] = 55
assert lst == [1,3,55,1,8,9,2,6]

// each
lst = [1,3,4,1,8,9,2,6]
lst.each { println it }
lst.eachWithIndex{ int entry, int i -> println "$i : $entry" }

total = 0
lst.each { total += it }
assert total == 34

doubled = []
doubled2 = []
lst.each {
    doubled << it * 2       // << is leftShift() i.e., push values onto an array
    doubled2.push( it * 2 ) // same
}
assert doubled == [2,6,8,2,16,18,4,12]
assert doubled2 == [2,6,8,2,16,18,4,12]

// collect
// collects the return values into a collection and returns it; there's an implicit return
assert lst.collect { it * 2 } == [2,6,8,2,16,18,4,12]

// find
lst = [4,3,1,2,4,1,8,9,2,6]
assert lst.find { it == 2 } == 2
assert !lst.find { it == 42 }
assert lst.find { it == 42 } == null
assert lst.find { it > 4 } == 8
assert lst.findAll { it > 4 } == [8,9,6]
assert lst.findAll { it == 2 } == [2,2]
assert lst.findIndexOf { it == 2 } == 3

//
lst = ['Programming', 'In', 'Groovy']
count = 0
lst.each { count += it.size() }
assert count == 19

// sum
assert [9,1,8,2,7,3].sum() == 30
assert lst.collect { it.size() }.sum() == 19

// inject
assert lst.inject(0) { carryOver, element -> carryOver + element.size() } == 19 // 3 iterations: 0+11; 11+2; 13+6

// join
assert lst.join( ' ' ) == 'Programming In Groovy'
lst[0] = ['Be', 'Productive']
assert lst == [['Be', 'Productive'], 'In', 'Groovy']

// flatten
lst = lst.flatten()
assert lst == ['Be', 'Productive', 'In', 'Groovy']
assert lst - ['Productive', 'In', 'Non-existent'] == ['Be', 'Groovy']

// * = the spread operator
assert lst*.size() == [2,10,2,6]
assert lst.collect() { it.size() } == [2,10,2,6] // same

// size of arrayList must
def words(a,b,c,d){
    "$a $b $c $d"
}
// more spread operator usage
assert words(*lst) == 'Be Productive In Groovy'

// Maps
def langs = ['C++' : 'Stroustrup', 'Java' : 'Gosling', 'Lisp' : 'McCarthy']
assert langs.getClass().name == 'java.util.LinkedHashMap'
assert langs.Java == 'Gosling'
assert langs['C++'] == 'Stroustrup'
assert langs.'C++' == 'Stroustrup'

// each
langs = ['C++' : 'Stroustrup', Java : 'Gosling', Lisp : 'McCarthy']
langs.each { entry -> println "$entry.key was written by $entry.value" }
// langs.each { language, author -> println "$language was written by $author" }    // same

// collect
assert langs.collect { language, author -> language.replaceAll( '[+]', 'P')}  == ['CPP', 'Java', 'Lisp']

// find
// TODO: why doesn't this return a regular Map  (i.e., [Java:'Gosling'] )?  What is a java.util.LinkedHashMap$Entry exactly?
entry = langs.find {language, author -> language.size() > 3} // == 'Java=Gosling'
assert entry.key == 'Java'
assert entry.value == 'Gosling'
// findAll
assert langs.findAll {language, author -> language.size() > 3} == [Java:'Gosling', Lisp:'McCarthy']

// any
assert langs.any { language, author -> language =~ "[^A-Za-z]"} == true     // Does *any* language contain a non-alphabetic character?

// every
assert langs.every { language, author -> language =~ "[^A-Za-z]"} == false  // Does *every* language contain a non-alphabetic character?

// groupBy
println 'groupBy'
friends = [briang:'Brian Goetz', brians:'Brian Sletten', davidb:'David Bock', davidg:'David Geary',scottd:'Scott Davis',scottl:'Scott Leberknight',stuarth:'Stuart Halloway']
groupByFirstName = friends.groupBy { it.value.split(' ')[0] }
assert groupByFirstName == [Brian:[briang:'Brian Goetz', brians:'Brian Sletten'], David:[davidb:'David Bock', davidg:'David Geary'], Scott:[scottd:'Scott Davis', scottl:'Scott Leberknight'], Stuart:[stuarth:'Stuart Halloway']]
assert groupByFirstName.getClass().name == 'java.util.LinkedHashMap'

groupByFirstName.each { firstName, buddies ->
    println "$firstName"
    println "$buddies"
}

groupByFirstName.each { firstName, buddies ->
    println "$firstName : ${ buddies.collect { key, fullName -> fullName }.join(', ')}"
}