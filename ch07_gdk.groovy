str = 'hello'
println str.dump()
println str.inspect()  // same as toString() if left unimplemented
// TODO why doesn't this assert?
// assert str.inspect() == 'hello'

lst = [1, 2]
lst.add(3)
lst.add(4)
assert lst.size() == 4
assert lst.contains(2) == true

lst = [1, 2]
lst.with {  // all method calls here delegate to lst
    add(3)
    add(4)
    assert size() == 4
    assert contains(2) == true
}

// This demonstrates that with sets the delegate to the object with was called on.
lst.with {
    println "this is $ch07_gdk"     // references this scripts implied(?) obj reference
    println "this is $owner"    // references this scripts implied(?) obj reference
    assert delegate == [1, 2, 3, 4] // references the lst object

}

// Sleep
 thread = Thread.start {
    println 'Thread #1 started'
    startTime=System.nanoTime()
    new Object().sleep(1000)
    endTime = System.nanoTime()
    println "Thread #1 done in ${(endTime - startTime) / 10**9} seconds"
}

new Object().sleep(100)
println "Let's ATTEMPT to interrupt Thread #1"
thread.interrupt()      // doesn't work; thread is NOT interrupted; Object().sleep() suppresses interruptedException
thread.join()


def playWithSleep(flag) {
    thread = Thread.start {
        println 'Thread #2 started'
        startTime = System.nanoTime()
        new Object().sleep(1000) {
            println "Thread #2 tnterrupted... " + it
            flag        // if false sleep continues as if not interrupted
        }
        endTime = System.nanoTime()
        println "Thread #2 done in ${(endTime - startTime) / 10**9} seconds"
    }

    thread.interrupt()
    thread.join()
}

playWithSleep(true)     // Interrupt occurs
playWithSleep(false)    // Interrupt doesn't occur

// Indirect Property
class Car3 {
    int miles, fuelLevel
}

car3 = new Car3(fuelLevel: 80, miles: 25)

properties = ['miles', 'fuelLevel']     // pretend this came from user input

properties.each { name ->
    println "$name = ${car3[ name ]}"
}

car3[properties[1]] = 100

println "fuelLevel now is ${car3.fuelLevel}"