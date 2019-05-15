## Datastore Challenge 

Datastore program with a couple of query functions. 

Queries: 
* -r  (read file passed in) 
* -s  (select data from datastore)
* -o  (order selected data from datastore)
* -f  (filter selected data from datastore)

### To Run
The dataset is in the project. I have not made it ready any psv file with any name yet, but the data in this one could be changed :) And then it's ran with this command
 
    ./gradlew run --args="-r ./dataset.psv"

To select and order the data from the datastore

    ./gradlew run --args="-s title,stb,time,rev -o rev"

      
<img src="./images/Screen Shot 2019-05-15 at 3.27.01 PM.png" width="700" />

To select and then order by more than one field 

    ./gradlew run --args="-s title,stb,time,rev,date  -o rev,date"
    
<img src="./images/Screen Shot 2019-05-15 at 3.29.28 PM.png" width="700" />

To select and then filter data from the datastore

    ./gradlew run --args="-s stb,title,date -f date=2010-01-03"

<img src="./images/Screen Shot 2019-05-15 at 4.18.42 PM.png" width="700" />
  
    
      
### Design 

#### Importer and Datastore
So the way I approached the problem is I broke it down into pieces. 
The first part of this problem was to read from a pipe separated value file and to import the data into a datastore. Java has multiple ways to read from a file. I chose to use openCSV. 
For the datastore however I started with just one file. The problem with that is that when there is one file it can get pretty large pretty quickly. And then searching and filtering and ordering would be very slow. So I decided to divide the psv file into a datastore that separates the lines being read into different files. The files in the datastore are divided by year. When there is a lot of data we could also divide them by month instead, which would make the files smaller. 
The psv file is being read line by line instead of the whole file because otherwise we will get out of memory exception when it gets too large.
   
    while((line = in.readLine()) != null) {
      //process line here
    }

#### Query Tool
For the Query Tool part of this challenge I created a POJO class to be able to not only use it for the importing of data but to also get the parameters I need when selecting or filtering or ordering. The POJO is just a standard POJO class, with the constructor and the getters and setters for parameters.

For the arguments parsing I used Apache's Commons CLI to parse the command line arguments given. I haven't used this library before so it was cool to learn something new. 

selectData and filterData methods both return a list of custom object class. 

The way Order works is it uses ChainedComparitor to be able to sort the queries by more than one field.
