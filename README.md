# DA2020S1_Project
This is our group's project of DA in 2020S1.


## version 1.1
- Fix some algorithm bugs
- Deal with primary key conflict
- Alter printing in Server

### TODO
- add a select operation to show the real data in the database (data not yet committed should not be visible), should show same results for every client
- add a gui for client and/or server (possibly?)
- deadlock handling
- something else...

___

## version 1.0
- Add TCP connection between client and server
- Alter feedback of command to send them back to clients



### found bugs
- delete action takes effect immidiately (not after commit)
- while one client is updating data, another client still can delete the same data
- something wrong with commit and rollback action



1. client can commit and rollback successfully without doing previous action
2. after commiting, clients can also rollback



### TODO
- add a select operation to show the real data in the database (data not yet committed should not be visible), should show same results for every client
- add a gui for client and/or server (possibly?)
- something else...
