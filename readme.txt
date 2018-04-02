RIP.java

This program implements the RIP 2 Routing protocol.
Compile the following classes: 

1. RIP.java
2. CompositeObject.java

To run the program specify the number of neighbors, each neighbor IP and its cost as command line argument.

For example: 
java RIP 3 129.21.34.80 50 129.21.30.37 3 129.21.22.196 1


Sample Output: 
Inputs: 
1. For machine 129.21.22.196, input = java RIP 3 129.21.37.49 1 129.21.34.80 100 129.21.30.37 20
2. For machine 129.21.30.37, input = java RIP 3 129.21.37.49 3 129.21.22.196 20 129.21.34.80 4
3. For machine 129.21.34.80, input = java RIP 3 129.21.30.37 4 129.21.37.49 50 129.21.22.196 100
4. For machine 129.21.37.49, input = java RIP 3 129.21.34.80 50 129.21.30.37 3 129.21.22.196 1


Output for machine: 129.21.22.196



PRINTING ROUTING TABLE

Destination      Subnet Mask     Next Hop        Cost
129.21.37.0      255.255.255.0   /129.21.37.49    1
129.21.34.0      255.255.255.0   /129.21.34.80    100
129.21.30.0      255.255.255.0   /129.21.30.37    20

PRINTING ROUTING TABLE

Destination      Subnet Mask     Next Hop        Cost
129.21.37.0      255.255.255.0   /129.21.37.49    1
129.21.34.0      255.255.255.0   /129.21.30.37    24
129.21.30.0      255.255.255.0   /129.21.37.49    4

PRINTING ROUTING TABLE

Destination      Subnet Mask     Next Hop        Cost
129.21.37.0      255.255.255.0   /129.21.37.49    1
129.21.34.0      255.255.255.0   /129.21.37.49    8
129.21.30.0      255.255.255.0   /129.21.37.49    4

PRINTING ROUTING TABLE

Destination      Subnet Mask     Next Hop        Cost
129.21.37.0      255.255.255.0   /129.21.37.49    1
129.21.34.0      255.255.255.0   /129.21.37.49    8
129.21.30.0      255.255.255.0   /129.21.37.49    4

Output for machine: 129.21.30.37

PRINTING ROUTING TABLE

Destination      Subnet Mask     Next Hop        Cost
129.21.37.0      255.255.255.0   /129.21.37.49    3
129.21.34.0      255.255.255.0   /129.21.34.80    4
129.21.22.0      255.255.255.0   /129.21.22.196   20

PRINTING ROUTING TABLE

Destination      Subnet Mask     Next Hop        Cost
129.21.37.0      255.255.255.0   /129.21.37.49    3
129.21.34.0      255.255.255.0   /129.21.34.80    4
129.21.22.0      255.255.255.0   /129.21.37.49    4

PRINTING ROUTING TABLE

Destination      Subnet Mask     Next Hop        Cost
129.21.37.0      255.255.255.0   /129.21.37.49    3
129.21.34.0      255.255.255.0   /129.21.34.80    4
129.21.22.0      255.255.255.0   /129.21.37.49    4

Output for machine: 129.21.34.80

PRINTING ROUTING TABLE

Destination      Subnet Mask     Next Hop        Cost
129.21.37.0      255.255.255.0   /129.21.37.49    50
129.21.22.0      255.255.255.0   /129.21.22.196   100
129.21.30.0      255.255.255.0   /129.21.30.37    4

PRINTING ROUTING TABLE

Destination      Subnet Mask     Next Hop        Cost
129.21.37.0      255.255.255.0   /129.21.30.37    7
129.21.22.0      255.255.255.0   /129.21.30.37    8
129.21.30.0      255.255.255.0   /129.21.30.37    4

PRINTING ROUTING TABLE

Destination      Subnet Mask     Next Hop        Cost
129.21.37.0      255.255.255.0   /129.21.30.37    7
129.21.22.0      255.255.255.0   /129.21.30.37    8
129.21.30.0      255.255.255.0   /129.21.30.37    4

PRINTING ROUTING TABLE

Destination      Subnet Mask     Next Hop        Cost
129.21.37.0      255.255.255.0   /129.21.30.37    7
129.21.22.0      255.255.255.0   /129.21.30.37    8
129.21.30.0      255.255.255.0   /129.21.30.37    4

Output for machine: 129.21.37.49

PRINTING ROUTING TABLE

Destination      Subnet Mask     Next Hop        Cost
129.21.22.0      255.255.255.0   /129.21.22.196   1
129.21.34.0      255.255.255.0   /129.21.34.80    50
129.21.30.0      255.255.255.0   /129.21.30.37    3

PRINTING ROUTING TABLE

Destination      Subnet Mask     Next Hop        Cost
129.21.22.0      255.255.255.0   /129.21.22.196   1
129.21.34.0      255.255.255.0   /129.21.30.37    7
129.21.30.0      255.255.255.0   /129.21.30.37    3

PRINTING ROUTING TABLE

Destination      Subnet Mask     Next Hop        Cost
129.21.22.0      255.255.255.0   /129.21.22.196   1
129.21.34.0      255.255.255.0   /129.21.30.37    7
129.21.30.0      255.255.255.0   /129.21.30.37    3


