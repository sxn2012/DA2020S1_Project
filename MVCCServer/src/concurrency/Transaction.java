package concurrency;
import java.util.*;

/**
 * @Author: XIGUANG LI <xiguangl@student.unimelb.edu.au>
 * @Purpose: XIGUANGL
 **/
public class Transaction {



    private Integer tid;

    //Transaction log
    private ArrayList<HashMap<String,String>> rollback;


    public Transaction(Integer tid){


        this.tid = tid;
        this.rollback = new ArrayList<>();

    }



    /*Add a data item into table list
    * Person: data item
    * Return: String status*/
    public String add(Person p){
        ArrayList<Person> records = new ArrayList<>(Records.instance().getRecords());

        for (int i = 0; i < records.size(); i++) {
    		Person pi = records.get(i);
    		 if(visible(pi)&&p.getpid().equals(pi.getpid()))
    		 {
    		     return "Failure: id already exists";

    		 }
    	}


        int order = Records.addItemToRecords(p,null);

        //bind transaction id with this data item
        p.setcreated_tid(this.tid);
        p.setexpired_tid(0);
        //insert reverse operation into log
        HashMap<String,String> map = new HashMap<>(){
            {
                put("action","delete");
                put("order",String.valueOf(order));
            }

        };

        this.rollback.add(map);

        return "Success";
    }


    /* "Delete" data item from table list
    * Pid: unique identifier of the data item
    * Return: String status*/
    public String delete(Integer pid) {

        ArrayList<Person> records = new ArrayList<>(Records.instance().getRecords());
        for (int i = 0; i < records.size(); i++) {

            Person p = records.get(i);

            if(visible(p)&&p.getpid().equals(pid)){

                if (rowLocked(p)){

                    return "Failure: Row is locked by another transaction";
                }else{

                    p.setLastWrite_timestamp();
                    //bind transaction id with this potential "invalid" data item
                    p.setexpired_tid(this.tid);
                    //insert reverse operation into log
                    HashMap<String,String> map = new HashMap<>();
                    map.put("action","add");
                    map.put("order",String.valueOf(i));

                    this.rollback.add(map);
                    return "Success";
                }
            }

        }
        return "Failure: No such row";

    }


    /*Update a data item
    * Pid: unique identifier of the data item
    * Name: related attribute
    * Return: String status*/
    public String update(Integer pid,String name){
         
    	String delres=delete(pid);
    	if(!delres.equals("Success")) return delres;
    	else return add(new Person(pid,name));

         


    }



    /*Quick search for the select data item*/
    public String binarySearch(Integer pid,int start,int end,ArrayList<Person> dataSource){
        if(dataSource.isEmpty()){

            return "Failure: Database doesn't have any value";
        }
        if(start<=end){

            int middle = (start+end)/2;

            Person p = dataSource.get(middle);


            if (pid.intValue() == p.getpid().intValue()){
                p.setLastRead_timestamp();
                return p.getstr();

            } else if (pid.intValue() < p.getpid().intValue()) {

                return binarySearch(pid,start,middle-1,dataSource);
            } else {

                return binarySearch(pid,middle+1,end,dataSource);
            }
        }else{

            return "Failure: No such row";
        }

    }

    /*View the information for a particular data item
    * Pid: unique identifier of the data item
    * Return: String status*/
    public String select(Integer pid){

        ArrayList<Person> dataSource = this.fetch();

        return binarySearch(pid,0,dataSource.size()-1,dataSource);
    }

    /*View all visible data items in the table list
    * Return: String data information*/
    public String view() {


    	String output="";
    	for (Person p:this.fetch())
        {
        	output=output+p.getstr()+"\n\r   ";
        }
    	if(output.equals("")) return "Failure: Database doesn't have any value";
    	else return output;
    }

    /*whether the particular data item is visible to current transaction
    * Return: boolean*/
    public boolean visible(Person p){

        //this item is created by an uncommitted transaction, so this item should only visible
        // to its creator
        if (Records.instance().getActive().contains(p.getcreated_tid())&& !p.getcreated_tid().equals(this.tid)){
            return false;
        }

        //1. this item is "deleted" by a successful committed transaction,
        // so this item should not visible for all transactions
        //2. this item is "deleted" an uncommitted transaction, so this item
        // should only not visible for the transaction which "delete" it.
        if (p.getexpired_tid().intValue() !=0 && (!Records.instance().getActive().contains(p.getexpired_tid())||p.getexpired_tid()==this.tid)){
            return false;
        }

        return true;

    }

    /*Whether these is a transaction "write" to this item*/
    public boolean rowLocked(Person p){

        return p.getexpired_tid().intValue() !=0 && Records.instance().getActive().contains(p.getexpired_tid());

    }


    /*Sort the table list, so can apply binary search*/
    public ArrayList<Person> sortTable(ArrayList<Person> table){

        Comparator<Person> c = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                if(o1.getpid()>o2.getpid()){
                    return 1;
                }
                return -1;
            }
        };

        table.sort(c);

        return table;
    }



    public ArrayList<Person> fetch(){

        return filter(Records.instance().getRecords());
    }

    public ArrayList<Person> filter(ArrayList<Person> table){
        
        ArrayList<Person> result = new ArrayList<>();


        for (Person p:table) {
            if (visible(p)){
                result.add(p);
            }
            
        }

        return sortTable(result);

    }



    /*Commit a transaction*/
    public String commit(){

    	if(this.rollback.isEmpty()) return "Failure: Nothing to commit";
        for (HashMap<String,String> action: this.rollback){

            int index = Integer.parseInt(action.get("order"));
            Person p = Records.instance().getRecords().get(index);
            if (action.get("action") == "add"){


                //if this is a "read" operation in transaction A after the "write" operation
                // in transaction B, then transaction B should be failed when B commit.
                if (p.getLastRead_timestamp()>=p.getLastWrite_timestamp()){
                    rollback();
                    return "Failure: Commit fails, rollback automatically";
                }

            }else if(action.get("action") == "delete"){

                ArrayList<Person> table = new ArrayList<>(Records.instance().getRecords());
                table.remove(index);
                ArrayList<Person> filteredTable = filter(table);

                String response = binarySearch(p.getpid(),0,filteredTable.size()-1,filteredTable);

                if (!response.contains("Failure:")){

                    rollback();
                    return "Failure: Person "+ p.getpid() + " already exists, rollback automatically";
                }


            }


        }
        Records.instance().getActive().remove(this.tid);
        return "Success";
    }


    /*Abort the transaction*/
    public String rollback(){

    	if(this.rollback.isEmpty()) return "Failure: Nothing to rollback";
        Collections.reverse(this.rollback);
        ArrayList<Person> records = new ArrayList<>(Records.instance().getRecords());
        for (HashMap<String,String> action: this.rollback) {
            if (action.get("action") == "add"){

                int index = Integer.parseInt(action.get("order"));
                records.get(index).setexpired_tid(0);
            }else if(action.get("action") == "delete"){
                int index = Integer.parseInt(action.get("order"));
                records.get(index).setexpired_tid(this.tid);
            }
        }
        Records.instance().getActive().remove(this.tid);
        return "Success";

    }




	public Integer getTid() {
		return tid;
	}




	public void setTid(Integer tid) {
		this.tid = tid;
	}




	public ArrayList<HashMap<String, String>> getRollback() {
		return rollback;
	}




	public void setRollback(ArrayList<HashMap<String, String>> rollback) {
		this.rollback = rollback;
	}

}
