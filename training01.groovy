/*
def lst = [1,2,3,4,5]

lst.each { Integer item ->
    println item
}
*/


def numbers = [2,1,2,5] as Set  // cast an ArrayList to the best Set fit (which is LinkedHashSet)
assert numbers.getClass().name == 'java.util.LinkedHashSet'
assert numbers == [2,1,5] as Set

Set numbers2 = [2,1,2,5]
assert numbers2.getClass().name == 'java.util.HashSet'
assert numbers2 == [1,2,5] as Set

def langs = ['C++' : 'Stroustrup', Java : 'Gosling', Lisp : 'McCarthy']
assert langs.getClass().name == 'java.util.LinkedHashMap'

def lang = langs.find {language, author -> language.size() > 3} // == 'Java=Gosling'
assert lang.getClass().name == 'java.util.LinkedHashMap$Entry'
println lang.dump()

// TODO: why doesn't this return a regular Map  (i.e., [Java:'Gosling'] )?  What is a java.util.LinkedHashMap$Entry exactly?
langs = ['C++' : 'Stroustrup', Java : 'Gosling', Lisp : 'McCarthy']
assert lang.key == 'Java'
assert lang.value == 'Gosling'

// The Dan Vega
// https://github.com/cfaddict/springone-wrapup/blob/master/demos/console/result.groovy
// given an array of maps
def result = [
        [Date: '10/29', HOST2:5, HOST3:5, HOST1:5],
        [Date: '10/29', COMPLETED:5],
        [Date: '10/30', HOST2:6, HOST3:6, HOST1:6],
        [Date: '10/30', COMPLETED:6],
        [Date: '10/31', HOST2:7, HOST3:7, HOST1:7],
        [Date: '10/31', COMPLETED:7]
]

// produce a result that looks like this (group by date)
// [Date:10/29, HOST2:5, HOST3:5, HOST1:5, COMPLETED:5]

result.groupBy { it.Date }.collect { key,value -> value.collectEntries() }.each {
    println it
}