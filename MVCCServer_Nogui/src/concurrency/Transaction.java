package concurrency;
import java.util.*;

/**
 * @Author: XIGUANG LI <xiguangl@student.unimelb.edu.au>
 * @Purpose: XIGUANGL
 **/
public class Transaction {


	private Long tid;

	private ArrayList<HashMap<String,String>> rollback;


    public Transaction(Long tid){

        this.tid = tid;
        this.rollback = new ArrayList<>();

    }




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

        p.setcreated_tid(this.tid);
        p.setexpired_tid(Long.valueOf(0));
        HashMap<String,String> map = new HashMap<>(){
            {
                put("action","delete");
                put("order",String.valueOf(order));
            }

        };

        this.rollback.add(map);

        return "Success";
    }


    public String delete(Integer pid) {

        ArrayList<Person> records = new ArrayList<>(Records.instance().getRecords());
        for (int i = 0; i < records.size(); i++) {

            Person p = records.get(i);

            if(visible(p)&&p.getpid().equals(pid)){

                if (rowLocked(p)){
                    rollback();

                    return "Failure: Row is locked by another transaction, rollback automatically";
                }else{

                    p.setLastWrite_timestamp();
                    p.setexpired_tid(this.tid);
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


    public String update(Integer pid,String name){
         
    	String delres=delete(pid);
    	if(!delres.equals("Success")) return delres;
    	else return add(new Person(pid,name));

         


    }



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

    public String select(Integer pid){

        ArrayList<Person> dataSource = this.fetch();

        return binarySearch(pid,0,dataSource.size()-1,dataSource);
    }

    public String view() {


    	String output="";
    	for (Person p:this.fetch())
        {
        	output=output+p.getstr()+"\n\r   ";
        }
    	if(output.equals("")) return "Failure: Database doesn't have any value";
    	else return output;
    }

    public boolean visible(Person p){

        if (Records.instance().getActive().contains(p.getcreated_tid())&& !p.getcreated_tid().equals(this.tid)){
            return false;
        }

        if (!p.getexpired_tid().equals(Long.valueOf(0)) && (!Records.instance().getActive().contains(p.getexpired_tid())|| p.getexpired_tid().equals(this.tid))){
            return false;
        }

        return true;

    }

    public boolean rowLocked(Person p){

        return !p.getexpired_tid().equals(Long.valueOf(0)) && Records.instance().getActive().contains(p.getexpired_tid());

    }


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



    public String commit(){

    	if(this.rollback.isEmpty()) return "Failure: Nothing to commit";
        for (HashMap<String,String> action: this.rollback){

            int index = Integer.parseInt(action.get("order"));
            Person p = Records.instance().getRecords().get(index);
            if (action.get("action") == "add"){


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


    public String rollback(){

    	if(this.rollback.isEmpty()) return "Failure: Nothing to rollback";
        Collections.reverse(this.rollback);
        ArrayList<Person> records = new ArrayList<>(Records.instance().getRecords());
        for (HashMap<String,String> action: this.rollback) {
            if (action.get("action") == "add"){

                int index = Integer.parseInt(action.get("order"));
                records.get(index).setexpired_tid(Long.valueOf(0));
            }else if(action.get("action") == "delete"){
                int index = Integer.parseInt(action.get("order"));
                records.get(index).setexpired_tid(this.tid);
            }
        }

        Records.instance().getActive().remove(this.tid);
        return "Success";

    }




	public Long getTid() {
		return tid;
	}




	public void setTid(Long tid) {
		this.tid = tid;
	}




	public ArrayList<HashMap<String, String>> getRollback() {
		return rollback;
	}




	public void setRollback(ArrayList<HashMap<String, String>> rollback) {
		this.rollback = rollback;
	}

}
