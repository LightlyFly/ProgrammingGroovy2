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