# WGUPS Program Overview
## Algorithm Overview
The algorithm used to solve the WGUPS delivery problem is a nearest neighbor algorithm that chooses the nearest address to deliver to next.

The pseudocode is as follows:
* Algorithm takes in a list of packages (package list)
* Initialize a list to hold the final route (delivery route)
* Initialize a current node variable with the first item from the package list
    * Pop the first node from the package list
* While the package list still has items to iterate
  * Initialize a minimum distance variable to infinity
  * Loop all potential next nodes in the package list
    * Get distance between the current node and potential next node
    * If the distance between nodes is less than the current minimum distance, set the minimum distance to the distance and the closest node to the potential next node
  * Once the loop of all potential next nodes is complete...
  * Set the closest node's distance from previous node variable to the last minimum distance
  * Set the current node to the closest node
  * Append the current node to the delivery route while popping it from the package list
* Return the delivery route

Strengths of the algorithm include the simplicity to implement and reaching a relatively ideal route quickly. The code is easy to understand, and therefore maintain, and the algorithm does not have to check all possible routes so the runtime is relatively quickly.<br>

Two other algorithms that could be used are breadth first search and djikstra's shortest path. Breadth first search will visit all nodes a certain  distance away from a starting node and then all nodes the next distance away until all nodes are visited instead of visiting the next closest node to whichever is the current node.
Djikstra's shortest path will find the shortest path from each node to each other node but will not identify a shortest path that visits every node.
## Programming Environment
The programming environment used was the PyCharm Python IDE on a Windows PC.

## Space Time Complexity
Main Function/wgupsrouting: O(N). This part of the code iterates over the hashtable and the number of trucks and will grow linearly as the size of the number of packages or delivery fleet grows.<br>
Delivery Algorithm: O(N). This part of the code iterates over the package list and will grow linearly with the number of packages.<br>
Delivery Truck: O(N^2). The delivery truck iterates over every other package for each package to build the delivery route and will grow exponentially as the number of packages grows.<br>
Hash Table: O(1). With the assumption that the hash table is large enough to accommodate all packages in their own buckets, all hash table operations are  immediate operations.<br>
Package: O(1). Package class is really just a constructor with no methods that manipulate the data.<br>
Entire program: O(N^2). Worst time complexity from the major sections - the Delivery Truck.

## Scalability
The program can scale as needed to accommodate more packages as the hash table simply puts more items in each bucket as the number of items increases. All other aspects of the program simple iterate over all the packages.<br>
One segment that will not scale well is the zip code assignments for each truck, as the number of packages to a zip code increases past the capacity of the truck, return trips will be needed and a different loading algorithm would be useful.

## Maintenance
The software is easy to maintain as all code is well commented and explained. Additionally, type hints are given where needed to assist in making function calls easier to use.

## Hash Table
The main strength of the hash table includes the ability to store a large amount of data that can be accessed in an efficient number. The main weakness is that the data structure needs to be initialized with a larger initial size as the data grows, otherwise each bucket will be cumbersome to find a given item in.

The lookup function will change when the number of packages exceeds the initial size of the hash table. The lookup function will then have to iterate through items in a bucket if there are multiple, or use an algorithm to find the next bucket an item would go in if the initial bucket is taken.

The hash table will use more space as the number of packages to be delivered increases.

The number of trucks and number of cities will not change the look-up time as the hash table stores instances of the Package class.

Two other data structures that could be used:
* Dictionary - the build in dictionary functions very similarly to a hash table and stores items based on a key. The dictionary abstracts out some written methods and allows easier end user interaction.
* Binary tree - a tree could be used to store the Packages and each package would have children based on the package ID, starting from the first package. This would create a sort of heirarchy in the packages instead of each one being on the same level.
## Lessons Learned
Something I would do differently if I was doing this project again is to attempt to combine the route creation and delivery functions of the Delivery Truck into one function and choose the next node to deliver to as each package is delivered.
