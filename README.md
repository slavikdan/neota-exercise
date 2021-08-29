# neota-exercise
Neota Coding excercise

## How to build
Project is created as a simple maven project. Prerequisites for build are: Maven, Java 11.

**To build:** _mvn package_

**To trigger tests:** _mvn test_

**To trigger ITs:** _mvn verify_

## How to execute
Project is wrapped as an executable jar with dependencies. 
After a successful build a jar **neota-exercise-1.0-SNAPSHOT-jar-with-dependencies.jar**  is created in a **target** folder.

**To execute:** _java -jar neota-exercise-1.0-SNAPSHOT-jar-with-dependencies.jar_

application accepts two options: debug, sleep

### Options
**debug**: Just some debug output used for the application development

**sleep {value}**: Allows changing default task sleep duration. Default value is 60 seconds. Format is described here: [here](https://docs.oracle.com/javase/8/docs/api/java/time/Duration.html#parse-java.lang.CharSequence-). 
For example to start the application with the sleep duration value set to 10 seconds use: _java -jar neota-exercise-1.0-SNAPSHOT-jar-with-dependencies.jar sleep PT10S_

## Commands

| command        | parameter     | description                           |
|----------------|---------------|---------------------------------------|
| help           |               | prints all commands                   |
| add-workflow   | /path/to/file | loads a workflow from a file          |
| list-workflows |               | lists all stored workflow definitions |
| start-workflow | workflowId    | starts a workflow session             |
| list-sessions  |               | lists all workflow sessions           |
| session-state  | sessionId     | gets current session state            |
| resume-session | sessionId     | resumes a workflow session            |
| exit           |               | exit ;-)                              |

In case a command is creating an object (a workflow or a workflow session) it's id is printed as a command response.

## example for communication

% java -jar neota-exercise-1.0-SNAPSHOT-jar-with-dependencies.jar sleep PT10S   

OUTPUT: **I do recommend starting with command 'help' ;-)**

INPUT: _add-workflow /xxx/xxx/workflow_definition.json_

OUTPUT: definition stored with id 4b20e15e-52ff-4b5b-b079-1b69bd9c581f

INPUT: _list-workflows_

OUTPUT: **Existing workflow definitions:**

OUTPUT: **4b20e15e-52ff-4b5b-b079-1b69bd9c581f**

INPUT: _start-workflow 4b20e15e-52ff-4b5b-b079-1b69bd9c581f_

OUTPUT: **Started a workflow session with id 3e2997fc-0bd1-46e7-888b-90ffc462461b**

INPUT: _list-sessions_

OUTPUT: **Existing workflow sessions:**

OUTPUT: **3e2997fc-0bd1-46e7-888b-90ffc462461b**

OUTPUT: **Task1 completed**

OUTPUT: **Task2 completed**

INPUT: _session-state 3e2997fc-0bd1-46e7-888b-90ffc462461b_

OUTPUT: **lane2**

INPUT: _resume-session 3e2997fc-0bd1-46e7-888b-90ffc462461b_

OUTPUT: **Task3 completed**

INPUT: _session-state 3e2997fc-0bd1-46e7-888b-90ffc462461b_

OUTPUT: **lane3**

INPUT: _resume-session 3e2997fc-0bd1-46e7-888b-90ffc462461b_

OUTPUT: **NOP completed**

OUTPUT: **Task4 completed**

OUTPUT: **Ended**

INPUT: _session-state 3e2997fc-0bd1-46e7-888b-90ffc462461b_

OUTPUT: **Ended**

INPUT: exit

OUTPUT: Have a nice one :-)
