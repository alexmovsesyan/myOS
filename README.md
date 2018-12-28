# Alex's Operating System

### Contents

-This Operating System contains UserThreads that read commands from text files
-Users write to text files on the Disk, managed by the DirectoryManager
-Users can also print files by creating PrintJobThreads which makes sure each Printer only has one job
-Each Printer writes to files in the output directory
-The ResourceManager, implemented as a semaphore, uses synchronized methods to allocated a resource like a Disk or a Printer to a specific Thread

### Info

-Each user is assigned a color
-Each disk and printer will be written in the color of the user that sent the command

-To view the full set of commands each user recieves, press the button under each user to expand the list

-The program will start at speed slow, which can be changed throughout execution

### Running the Operating System
-run the main.java file in the src directory
# myOS
