# **Alfalfa Commandline Version**
#### 1. Clone AlfalfaJava and compile.

```git clone git@github.com:JamesArthurHolland/AlfalfaJava```

```cd AlfalfaJava/src && javac *.java```
 
#### 2. Clone patterns and add to pattern directory file. 
Only one pattern directory can reside in this file. To run Alfalfa with different patterns, make a new install 
or change the patternDirectory.txt file to point to the working directory of the new pattern directory.

```git clone git@github.com:JamesArthurHolland/AlfalfaPatternsPhalconPHP```

```cd AlfalfaPatternsPhalconPHP && pwd >> where/installed/AlfalfaJava/src/patternDirectory.txt```

#### 3. Make an alias for Alfalfa. 
Consider that multiple installs can have different names eg alfalfaphp alfalfajava etc.

```echo "alias alfalfa='java -cp ~/AlfalfaJava/src/ Compiler'" >> ~/.bash_profile```

#### 4. Reinitialise your bash profile.

```. ~/.bash_profile```

