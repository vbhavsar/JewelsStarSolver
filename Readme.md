# What? #
Solver for Jewels Star. See Example below for a few more details. 

# Why? #
[See Quest](https://gist.github.com/vbhavsar/a080073aa427b53c2e73)


# Example #

## v1 ##
### Screenshot ####
![Sample data](https://raw.githubusercontent.com/vbhavsar/JewelsStarSolver/master/resources/samples/jewel.png)

### Classification ###
#### With only the center point in the dataset ####
<pre>
[0 4 4 6 6 4 2]
[0 0 4 0 0 0 0]
[6 4 6 3 2 4 3]
[0 2 6 0 3 2 3]
[0 3 4 3 2 0 0]
[3 4 6 3 0 6 0]
[6 3 1 0 0 0 5]
[2 0 2 0 4 0 6]
[3 0 0 0 4 4 6]
</pre>

#### Observation ####
Almost all of the basic jewels are correctly classified into separate classes (there are some exceptions such as [1][0]). Understandably, the special jewels, namely [0][5], [6][2] and [6][6], are not classified correctly at all. Decent for a naiive algorithm. The challenge will be to improve upon this so that the classification is exact.

Breaking the problem further, there are 2 main issues to tackle:
1. identify the right number of classes (in this case, 7) 
2. identify the specialized jewels into the correct classes. 

Both of which seem to pose significant challenges.

### Demo ###
[Click to see the video](http://youtu.be/89ODh3fhpTY)

