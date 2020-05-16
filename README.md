# MVCC Implementation
  This is our group's project of DA in 2020S1,  which implements the MVCC algorithms in Java, allowing different clients to access the data stored in server simultaneously without leading to inconsistent state. The operation of clients is quite similar to the real database system, including:

- Insert data

- Update data

- Select data

- Delete data

- Commit

- Rollback

The data format is (Student ID, Student Name), where Student ID, primary key, is an integer and Student Name is a string.

## Note

1. **Please load the jar libraries first before running the system**

   Jar Files are available in:

- [org-json.jar](https://jar-download.com/artifacts/org.json)

- [com.google.code.gson.jar](https://search.maven.org/artifact/com.google.code.gson/gson/2.8.6/jar)

2. **After starting the server, remember to bind a port number where clients are able to connect.**

3. **After starting the client, remember to set the IP address and port number of the server to which the client will connect.**

4. **When connected to server, the client needs to login first before making any transactions.**

5. **Before leaving the system, the client needs to logout.**

## Running Environment

- Java version: 1.8 or higher

- System Requirements: Windows, Linux or Mac OS 64 bit

- Use `java -jar <jar package name>` to start the system

## CopyRight

Copyright © 2020, [Xinnan SHEN](https://github.com/sxn2012), [Xiguang Li](https://github.com/lixiguang), [Chaoxian Zhou](https://github.com/CcAnL), [Huidu Lu](https://github.com/Huidul), released under the [GPL-3.0 License](https://github.com/sxn2012/DA2020S1_Project/blob/master/LICENSE).
