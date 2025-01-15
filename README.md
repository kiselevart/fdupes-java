# jdupes: An fdupes clone written in Java
### Setup:
--------------------------------------------------------------------
Navigate to the root directory (/fdupes-java)
Create the jar binary:
```bash
mvn package
```
To run the binary:
```bash
java -jar target/jdupes-1.0-jar-with-dependencies.jar   
```
Default command:
```bash
java -jar target/jdupes-1.0-jar-with-dependencies.jar -f ../ -a bbb -p -c   
```

### Options:
--------------------------------------------------------------------
- -f: prints the total count of duplicate files 
- -a: specifies the algorithm used (bbb, sha256, md5)
- -p prints relative paths of all duplicates grouped together
- -f specifies path to folder, must be provided.