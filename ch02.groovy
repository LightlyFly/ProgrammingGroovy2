// for in
for( i in 0..2 ) {print 'ho '}

// upto
0.upto( 2 ) {print "$it "}

// times
3.times {print "$it "}

// step
0.step( 10, 2 ) {println "$it "}

// println 'svn help'.execute().text
assert 'svn help'.execute().getClass().name == 'java.lang.ProcessImpl'

// ?.   safe-navigation operator
def foo( String str ){
    str?.reverse()  // reverse is only called if str is not null
}

assert foo( 'evil' ) == 'live'
assert foo( null ) == null

// exception handling
def openFile( fileName ){
    new FileInputStream( fileName )
}

try {
    openFile( 'menoexist')
    assert false
} catch( FileNotFoundException ex ){
    assert ex.getClass().name == 'java.io.FileNotFoundException'
    assert ex.getMessage() == 'menoexist (The system cannot find the file specified)'
    assert "$ex" == 'java.io.FileNotFoundException: menoexist (The system cannot find the file specified)'  // the double quotes stringifies the object (I think)
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
assert car.year == 2008


class Car2 {
    final year
    private miles = 0

    Car2( theYear ) { year = theYear }

    def getMiles(){
        miles
    }

    // TODO: why does the book use 'private' here?  Pg. 20 states Groovy does not honor 'private', so I presume it is public either way.
    private void setMiles( miles ){
        throw new IllegalAccessException( 'you cannot change miles' )
    }

    def drive( dist ){ if (dist > 0)  miles += dist }
}

def car2 = new Car2( 2012 )

car2.drive( 10 )
assert car2.miles == 10

try {
    car2.year = 1984
    assert false
} catch( groovy.lang.ReadOnlyPropertyException ex ) {
    assert ex.message == 'Cannot set readonly property: year for class: Car2'
}

try {
    car2.miles = 12
    assert false
} catch( IllegalAccessException ex ){
    assert ex.message == 'you cannot change miles'
}

// Skip 'get' fn for most property getters
assert Calendar.instance.getClass().name == 'java.util.GregorianCalendar'

class Robot {
    def type, height, width
    def access( location, weight, fragile ){
        "Received fragile? $fragile, weight: $weight, loc: $location"
    }
}
robot = new Robot( type: 'arm', width: 10, height: 40)
assert "$robot.type, $robot.height, $robot.width" == 'arm, 40, 10'
assert robot.access( a:30, y: 20, z: 10, 50, true ) == 'Received fragile? true, weight: 50, loc: [a:30, y:20, z:10]'    // Here all the name-value args go in to 'location' param as a collection array (see next comment)
assert robot.access( 50, true, a:30, y: 20, z: 10 ) == 'Received fragile? true, weight: 50, loc: [a:30, y:20, z:10]'    // If arg count doesn't match method signature AND the excess are name-value pairs, Groovy groups this as a collection into the FIRST parameter... always.  So Grroovy plays around the arg order in this case.

// The "solution" to craziness above is to be explicit that location is indeed a Map.
class Robot2 {
    def type, height, width
    def access( Map location, weight, fragile ){
        "Received fragile? $fragile, weight: $weight, loc: $location"
    }
}

robot2 = new Robot2()
// However, the arg order can still be out of order.
assert robot2.access( 10, true, a:1, y: 3, z: 5 ) == 'Received fragile? true, weight: 10, loc: [a:1, y:3, z:5]'

