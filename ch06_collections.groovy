/**
 * Created by epierce on 12/16/13.
 */

lst = [1,3,4,1,8,9,2,6]
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

// collects the return values into a collection and returns it; there's an implicit return
assert lst.collect { it * 2 } == [2,6,8,2,16,18,4,12]

lst2 = [4,3,1,2,4,1,8,9,2,6]
assert lst2.find { it == 2 } == 2
assert !lst2.find { it == 42 }
assert lst2.find { it == 42 } == null
assert lst2.find { it > 4 } == 8
assert lst2.findAll { it > 4 } == [8,9,6]
assert lst2.findAll { it == 2 } == [2,2]
assert lst2.findIndexOf { it == 2 } == 3

lst3 = ['Programming', 'In', 'Groovy']
count = 0
lst3.each { count += it.size() }
assert count == 19

assert [9,1,8,2,7,3].sum() == 30
assert lst3.collect { it.size() }.sum() == 19

println lst3.inject(0) { carryOver, element -> carryOver + element.size() } // 3 iterations: 0+11; 11+2; 13+6