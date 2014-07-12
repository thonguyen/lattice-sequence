-> Initial closure system saved in examples//ExampleISBijectiveComponents/ExampleISInitialClosureSystem.txt: 
a b c d e f 
 -> f 
c  -> b 
c e  -> a d 
b  -> c 
b e  -> a d 
a d  -> b c e 

-> Closed set or concept lattice saved in examples//ExampleISBijectiveComponents/ExampleISLattice.dot
-> Reduced lattice saved in examples//ExampleISBijectiveComponents/ExampleISReducedLattice.dot
-> Table of the reduced lattice saved in examples//ExampleISBijectiveComponents/ExampleISTable.txt
Observations: [d,e,f] [a,e,f] [b,c,d,f] [a,b,c,f] 
Attributes: [e,f] [d,f] [b,c,f] [a,f] 
[d,e,f] : [e,f] [d,f] 
[a,e,f] : [e,f] [a,f] 
[b,c,d,f] : [d,f] [b,c,f] 
[a,b,c,f] : [b,c,f] [a,f] 

-> Canonical basis saved in examples//ExampleISBijectiveComponents/ExampleISCanonicalBasis.txt: 
a b c d e f 
c f  -> b 
c e  -> a b d 
c d  -> b 
b f  -> c 
b e  -> a c d 
b d  -> c 
a d  -> b c e 
a c  -> b 
a b  -> c 

-> Canonical direct basis of the reduced lattice saved in examples//ExampleISBijectiveComponents/ExampleISCanonicalDirectBasis.txt: 
a b c d e f 
c f  -> b 
c e  -> a b d 
c d  -> b 
b f  -> c 
b e  -> a c d 
b d  -> c 
b c e  -> a d 
a d  -> b c e 
a c  -> b 
a b  -> c 

-> Dependency Graph  of the reduced lattice saved in examples//ExampleISBijectiveComponents/ExampleISDependencyGraph.dot 
-> Minimal generators  of the reduced lattice are [[c,f], [c,e], [c,d], [b,f], [b,e], [b,d], [b,c,e], [a,d], [a,c], [a,b]]
