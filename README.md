## Datastore Challenge 

### To Run
The dataset is in the project. I have not made it ready any psv file with any name yet, but the data in this one could be changed :) And then it's ran with this command
 
    ./gradlew run --args="-r ./dataset.psv"

To select and order the data from the datastore

    ./gradlew run --args="-s stb,title,date -o date,title"

To select and then filter data from the datastore

    ./gradlew run --args="-s stb,title -f title='the hobbit'"
     
### Design 

So the way I approached the problem is I broke it down into pieces. 
The first part of this problem was to read from a pipe separated value file and to import the data into a datastore. Java has multiple ways to read from a file. I chose to use openCSV. 
For the datastore however I started with just one file. However, the problem with that is that when there is one file it can get pretty large pretty quickly. And then searching and filtering and ordering would be very slow. So I decided to divide the psv file into a datastore that separates the lines being read into different files. The files in the datastore are divided by year. When there is a lot of data we could also divide them by month instead, which would make the files smaller. 
The psv file is being read line by line instead of the whole file because otherwise we will get out of memory exception when it gets too large.
   
    while((line = in.readLine()) != null) {
      //process line here
    }
