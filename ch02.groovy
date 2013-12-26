// for in
for( i in 0..2 ) {print 'ho '}

// upto
0.upto( 2 ) {println "$it "}

// times
3.times {println "$it "}

// step
0.step( 10, 2 ) {println "$it "}

// println 'svn help'.execute().text

println 'svn help'.execute().getClass().name

// ?.   safe-navigation operator
def foo( String str ){
    str?.reverse()  // reverse is only called if str is not null
}

println foo( 'evil' )
println foo( null )

// exception handling
def openFile( fileName ){
    new FileInputStream( fileName )
}

try {
    openFile( 'menoexist')
} catch( FileNotFoundException e ){
    println "Oops: $e"
}

class Wizard{
    def static learn( trick ){
        //
        println trick
        this        // makes chaining possible
    }
}


Wizard.learn( 'hapula' )
    .learn( 'bopula' )
    .learn( 'crapula' )



class Car {
    def miles = 0
    final year

    Car( theYear ) { year = theYear }
}

Car car = new Car(2008)
car.miles = 25  // goes through implicitly created setMiles fn
println car.dump()

