// for in
for( i in 0..2 ) {print 'ho '}

// upto
0.upto( 2 ) {print "$it "}

// times
3.times {print "$it "}

// step
0.step( 10, 2 ) {println "$it "}

// different results depending on which OS it's ran under
// assert 'svn help'.execute().getClass().name == 'java.lang.ProcessImpl'   // Win
// assert 'svn help'.execute().getClass().name == 'java.lang.UNIXProcess''  // Linux

// ?.   safe-navigation operator
def foo( String str ){
    str?.reverse()  // reverse is only called if str is not null
}

assert foo( 'evil' ) == 'live'
assert foo( null ) == null

// exception handling
def openFile( String fileName ){
    new FileInputStream( fileName )
}

try {
    openFile( 'menoexist')
    assert false
} catch( FileNotFoundException ex ){
    assert ex.getClass().name == 'java.io.FileNotFoundException'
    // assert ex.getMessage() == 'menoexist (The system cannot find the file specified)'
    // assert "$ex" == 'java.io.FileNotFoundException: menoexist (The system cannot find the file specified)'  // the double quotes stringifies the object (I think)
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
} catch( ReadOnlyPropertyException ex ) {
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

// Optional Parameters
def log( x, base=10 ){
    Math.log( x ) / Math.log( base )
}

assert log( 1024 ) == 3.0102999566398116
assert log( 1024, 10 ) == 3.0102999566398116
assert log( 1024, 2 ) == 10.0

// Groovy treats trailing array param as zero or more args.
def task( name, String[] details ){
    "$name - $details"
}

assert task ('Call', '123-456-7890') == 'Call - [123-456-7890]'
assert task ('Call', '123-456-7890', '231-546-0987') == 'Call - [123-456-7890, 231-546-0987]'
assert task ('Check Mail') == 'Check Mail - []'

// Multiple Assignments
def splitName( fullName ) { fullName.split(' ') }
def ( firstName, lastName ) = splitName( 'James Bond' )
assert firstName == 'James'
assert lastName == 'Bond'

//  swap vars
( firstName, lastName ) = [ lastName, firstName ]
assert firstName == 'Bond'
assert lastName == 'James'

def (cat, mouse) = ['Tom','Jerry','Spike','Tyke']
assert cat == 'Tom'
assert mouse == 'Jerry'

def (first, second, third ) = ['Tom','Jerry']
assert first == 'Tom'
assert second == 'Jerry'
assert third == null

// Boolean eval
def str = 'hello'
if( str ){
    assert true
} else {
    assert false
}

var1 = null
assert !var1
var2 = [1,2,3]
assert var2
var3 = []
assert !var3

// operator overload
def charCol1 = []
for( ch = 'a'; ch < 'd'; ch++ ){
    charCol1 << ch
}

assert charCol1 == ['a','b','c']

def charCol2 = []
for( ch in 'a'..'c'){
    charCol2 << ch
}

assert charCol2 == ['a','b','c']

lst = ['Hello']
lst << 'there'
assert lst == ['Hello','there']

class ComplexNumber{
    def real, imaginary

    def plus(other){
        new ComplexNumber( real: real + other.real, imaginary: imaginary + other.imaginary )
    }

    String toString(){ "$real ${imaginary > 0 ? '+' : ''} ${imaginary}i"}
}
c1 = new ComplexNumber( real: 1, imaginary: 2)
c2 = new ComplexNumber( real: 4, imaginary: 1)
assert (c1 + c2).toString() == '5 + 3i'
println c1 + c2

// for-each
String[] greetings = ['Hello','Hi','Howdy']
def greetingsCatch1 = []
for( String greet : greetings){     // must provide type or 'def' when doing the Java5ish Groovy way
    greetingsCatch1 << greet
}
assert greetingsCatch1 == ['Hello','Hi','Howdy']

def greetingsCatch2 = []
for( greet in greetings ){     // Groovier way; don't need to declare the type or 'def'
    greetingsCatch2 << greet
}
assert greetingsCatch2 == ['Hello','Hi','Howdy']

// enum
enum CoffeeSize { SHORT, SMALL, MEDIUM, LARGE, MUG }
def orderCoffee( size ){
    msg = "coffee order received for size $size: "
    switch( size ){
        case [CoffeeSize.SHORT, CoffeeSize.SMALL]:
            msg += "you're health conscious"
            break
        case CoffeeSize.MEDIUM..CoffeeSize.LARGE:
            msg += 'you gotta be a programmer'
            break
        case CoffeeSize.MUG:
            msg += 'you should try Caffeine IV'
            break
    }
    msg
}

assert orderCoffee( CoffeeSize.SMALL ) == 'coffee order received for size SMALL: you\'re health conscious'
assert orderCoffee( CoffeeSize.LARGE ) == 'coffee order received for size LARGE: you gotta be a programmer'

for( size in CoffeeSize.values() ){
    print "$size "
}

enum Methodologies{
    Evo(5),
    XP(21),
    Scrum(30);

    final int daysInIteration

    Methodologies( days ){ daysInIteration = days }

    def iterationDetails(){
        "${this} recommends $daysInIteration days for iteration"
    }
}

for( methodology in Methodologies.values() ){
    println methodology.iterationDetails()
}

assert Methodologies.Evo.iterationDetails() == 'Evo recommends 5 days for iteration'
assert Methodologies.Scrum.iterationDetails() == 'Scrum recommends 30 days for iteration'

// varargs
def receiveVarArgs( int a, int... b){
    "You passed $a and $b"
}

def receiveArray( int a, int[] b){
    "You passed $a and $b"
}

assert receiveVarArgs(1,2,3,4,5) == "You passed 1 and [2, 3, 4, 5]"
assert receiveArray(1,2,3,4,5) == "You passed 1 and [2, 3, 4, 5]"
assert receiveVarArgs( 1, [2,3,4,5] as int[]) == "You passed 1 and [2, 3, 4, 5]"
assert receiveArray( 1, [2,3,4,5]  as int[]) == "You passed 1 and [2, 3, 4, 5]"

assert ([1,2,3]).getClass().name == 'java.util.ArrayList'
assert ([1,2,3] as int[]).getClass().name == '[I'       // [I is int[]

// annotations

// Static Import
import static Math.random as rand
import groovy.lang.ExpandoMetaClass as EMC

double value = rand()
def metaClass = new EMC(Integer)
def metaClass2 = new groovy.lang.ExpandoMetaClass(Integer)
assert metaClass.getClass().name == 'groovy.lang.ExpandoMetaClass'
assert metaClass2.getClass().name == 'groovy.lang.ExpandoMetaClass'

// Generics
// @Canonical
import groovy.transform.*

@Canonical(excludes = "lastName, age")
class Person{
    String firstName
    String lastName
    int age
}

def sara = new Person( firstName: 'Sara', lastName: 'Walker', age:49)
println sara
assert "$sara" == "Person(Sara)"
assert sara.toString() == "Person(Sara)"

// @Delegate
class Worker{
    def work(){ 'get work done'}
    def analyze(){ 'analyze...'}
    def writeReport(){ 'get report written'}
}
class Expert{
    def analyze(){ 'expert analyze...'}
}
class Manager{
    // If we switch the order of these two, the Worker's analyze fn will be used.
    @Delegate Expert expert = new Expert()
    @Delegate Worker worker = new Worker()
}
def bernie = new Manager()
assert bernie.analyze() == 'expert analyze...'
assert bernie.work() == 'get work done'
assert bernie.writeReport() == 'get report written'

// @Immutable
@Immutable
class CreditCard{
    String cardNumber
    int creditLimit
}

def cc = new CreditCard('4000-1111-2222-3333',1000)
assert "$cc" == 'CreditCard(4000-1111-2222-3333, 1000)'

// @Lazy
class Heavy{
    def size = 10
    Heavy(){ println "Creating Heavy with $size"}
}

class AsNeeded {
    def value

    @Lazy Heavy heavy1 = new Heavy()

    // TODO: what's up w/the {}() syntax here?  Seem to work the same without it (i.e., just new Heavy( size: value)
    @Lazy Heavy heavy2 = { new Heavy( size: value ) }() // TODO: Is this the same as instantiating via an implicit constructor and then calling an implicit setSize method?


    AsNeeded(){ println 'Created AsNeeded'}
}

def asNeeded  = new AsNeeded( value: 1000)
println asNeeded.heavy1.size    // heavy1 instantiated here
println asNeeded.heavy1.size
println asNeeded.heavy2.size    // TODO: heavy2 instantiated here (via an implicit default constructor?), then assigned 1000 (via an implicit setter).

// @Newify
@Newify([Person, CreditCard])
def fluentCreate(){
    println Person.new( firstName: "John", lastName: "Doe", age: 20 )   // Ruby style (?)
    println Person( firstName: "John", lastName: "Doe", age: 20 )       // Python style instantiation w/o new (?)
    println CreditCard( "1234-5678-1234-5678", 2000)                    // Python style instantiation w/o new (?)
}

fluentCreate()

// @Singleton
@Singleton( lazy = true )
class TheUnique{
    TheUnique(){ println 'Instance created' }

    def hello() { println 'hello' }
}

println 'Accessing TheUnique'
TheUnique.instance.hello()      // with lazy=true; Singleton waits to instantiate till first accessed.
TheUnique.instance.hello()

// Gotchas
// equals
str1 = 'hello'
str2 = str1
str3 = new String('hello')
str4 = 'Hello'

assert str == str2
assert str == str3
assert str != str4

assert str1.is( str2 )
assert ! str1.is( str3 )
assert ! str1.is( str4 )


class A{
    boolean equals(other){
        println 'equals called'
        false
    }
}

class B implements Comparable{
    boolean equals(other){
        println 'equals called'
        false
    }

    int compareTo( other ){
        println 'compareTo called'
        0
    }
}

new A() == new A()  // normally, equals is called when comparing using ==
new B() == new B()  // but here compareTo is called!  implementing Comparable makes this occur

// type checking
try {
    Integer val = 4
    val = 'hello'
} catch( e ){
    assert e.message == "Cannot cast object 'hello' with class 'java.lang.String' to class 'java.lang.Integer'"
}

try {
    Integer val2 = 4
    val2.blah()
} catch( e ){
    println e.getMessage()
}

// prevent closure / inner class confusion
class Calibrator{
    Calibrator( calcBlock){
        print 'using...'
        calcBlock()     // execute closure
    }
}

// make sure to stick the closure inside parens to avoid inner class creation confusion by Groovy
new Calibrator({ println 'the calc provided' })
def calc = { println 'another calc provided'}
new Calibrator(  calc )


// semicolon!

class Semi{
    def val = 3;    // needed here for next block

    // "instance initializer" (?)
    {
        println 'instance initializer called'
    }
}

println new Semi()

int[] arr = [1,2,3]
def arr2 = [4,5,6]

assert arr.getClass().name == '[I'      // is a int[]
assert arr2.getClass().name == 'java.util.ArrayList'

