# Distributed-Code-Jam-Java-Local-Simulator
Using java threads I created a sample code to use for simulations in local computer :)

The code is:
  Main: My solution for baby blocks.
  baby_block: This is the problem library. 
  Messgae: This is the local message library.
  
The idea is to use a local implementation of the message library to achieve a way to run the code in a single process using multiple thread in java environment.

# Notice
In order to submit the code you need to delete the first few lines in Main.java:
    
    //############ COMMENT THOSE LINE BEFORE SUBMISSION!!
    private Message message;

    public Main(Message message) {
        this.message = message;
    }
    //############ THOSE LINES! ^^
Those lines are used in the local environment, and will couse compiler error in the google jam (This is because the local message library does not exist in google code jam environment).

I hope this will be of use for someone :D

Ido
