//
def sum(n){
    total=0
    for(int i=2; i<=n; i+=2){
        total +=i
    }
    total
}

assert sum(10) == 30

def product(n){
    prod=1
    for(int i=2; i<=n; i+=2){
        prod *=i
    }
    prod
}

assert product(10) == 3840


def sqr(n){
    squared=[]
    for(int i=2; i<=n; i+=2){
        squared << i ** 2
    }
    squared
}

assert sqr(10) == [4, 16, 36, 64, 100]

def pickEven(n, block){
    for( int i=2; i <= n; i+= 2){
        block( i )
    }
}

pickEven( 10, { println it })

// alt syntax
// pickEven( 10 ) { evenNum -> println evenNum }

total = 0
pickEven( 10 ) { total += it }
assert total == 30

product = 1
pickEven( 10 ){ product *= it }
assert product == 3840

def totalSelectValues( n, closure ){
    total = 0
    for( i in 1..n ){
        if( closure( i )) { total += i }
    }
    total
}

assert totalSelectValues( 10 ){ it % 2 == 0 } == 30

def isOdd = { it % 2 != 0 }
assert totalSelectValues( 10, isOdd ) == 25


class Equipment {
    def calculator
    Equipment( calc ) { calculator = calc }
    def simlulate(){
        calculator()
    }
}

def eq1 = new Equipment( { 'Calculator 1' })
def aCalculator = { 'Calculator 2' }
def eq2 = new Equipment( aCalculator )

assert eq1.simlulate() == 'Calculator 1'
assert eq2.simlulate() == 'Calculator 2'

// Passing params to closures
def tellFortune( closure ){
    closure new Date( '09/20/2012'), 'Your day is filled with ceremony'
}
assert tellFortune() { Date date, String fortune ->   // types are optional
    "Fortune for ${date} is '${fortune}"
} == "Fortune for Thu Sep 20 00:00:00 CDT 2012 is 'Your day is filled with ceremony"

// closures as resource cleanup
new FileWriter('output.txt').withWriter { writer -> writer.write('a') }

class Resource {
    def open() { print 'open... '}
    def close() { print 'close... '}
    def read() { print 'read... '}
    def write() { print 'write... '}

    def static use( closure ){
        def r = new Resource()
        try {
            r.open()        // ensure we 'open' the resource
            closure( r )
        } finally {
            r.close()       // ensure we 'close' the resource
        }
    }
}

Resource.use { res ->
    res.read()
    res.write()
}

// closures and co-routines
def iterate(n, closure) {
    1.upto(n){
        println "In iterate with value ${it}"
        closure( it )
    }
}

println 'Calling iterate'
total = 0
iterate(4){
    total += it
    println "In closure total so far is ${total}"
}

println 'Done'

// curried closure
def tellFortunes(closure) {
    Date date = new Date('09/20/2012')
    postFortune = closure.curry(date)   // created an alias for closure arg called postFortune
    postFortune 'Your day is filled with ceremony'
    postFortune "They're features, not bugs"

    superPostFortune = closure.curry( date, "fortunes are for Mojo!")  // curry both args
    superPostFortune()                              // now call closure w/no args
}

tellFortunes() { date, fortune ->
    println "Fortune for ${date} is '${fortune}'"

}

