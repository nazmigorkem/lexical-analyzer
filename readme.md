### Requirements
- Java version openjdk 20 or later

### Commands

- Change your current directory to this projects folder.
- Execute `javac ./src/Main.java`
- Execute `java src.Main`

### Input

- Change the `input.txt` file which is in `src` folder.

### Example Input and Output

#### Input
```
(define (fibonacci n)
  (let fib ([prev 0]
            [cur 1]
            [i 0])
    (if (= i n)
        cur
        (fib cur (+ prev cur) (+ i 1)))))
```
#### Output
```
LEFTPAR 1:1
DEFINE 1:2
LEFTPAR 1:9
IDENTIFIER 1:10
IDENTIFIER 1:20
RIGHTPAR 1:21
LEFTPAR 2:3
LET 2:4
IDENTIFIER 2:8
LEFTPAR 2:12
LEFTSQUAREB 2:13
IDENTIFIER 2:14
NUMBER 2:19
RIGHTSQUAREB 2:20
LEFTSQUAREB 3:13
IDENTIFIER 3:14
NUMBER 3:18
RIGHTSQUAREB 3:19
LEFTSQUAREB 4:13
IDENTIFIER 4:14
NUMBER 4:16
RIGHTSQUAREB 4:17
RIGHTPAR 4:18
LEFTPAR 5:5
IF 5:6
LEFTPAR 5:9
IDENTIFIER 5:10
IDENTIFIER 5:12
IDENTIFIER 5:14
RIGHTPAR 5:15
IDENTIFIER 6:9
LEFTPAR 7:9
IDENTIFIER 7:10
IDENTIFIER 7:14
LEFTPAR 7:18
IDENTIFIER 7:19
IDENTIFIER 7:21
IDENTIFIER 7:26
RIGHTPAR 7:29
LEFTPAR 7:31
IDENTIFIER 7:32
IDENTIFIER 7:34
NUMBER 7:36
RIGHTPAR 7:37
RIGHTPAR 7:38
RIGHTPAR 7:39
RIGHTPAR 7:40
RIGHTPAR 7:41
```