import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * @Author: XIGUANG LI <xiguangl@student.unimelb.edu.au>
 * @Purpose: XIGUANGL
 **/
public class Transaction {


    Integer tid;

    ArrayList<HashMap<String,String>> rollback;


    Transaction(Integer tid){

        this.tid = tid;
        this.rollback = new ArrayList<>();

    }

    public String add(Person p){
    	for (int i = 0; i < Records.instance().records.size(); i++) {
    		Person pi = Records.instance().records.get(i);
    		 if(p.getpid().equals(pi.getpid()))
    		 {
    		     if (visible(pi)){
                     return "Failure: id already exists";
                 }else if(Records.instance().active.contains(pi.getcreated_tid())){
    		         return "Failure: another transaction attempt to create this item";
                 }

    		 }
    	}
        p.setcreated_tid(this.tid);
        p.setexpired_tid(0);
        HashMap<String,String> map = new HashMap<>(){
            {
                put("action","delete");
                put("order",String.valueOf(Records.instance().records.size()));
            }
        };
        this.rollback.add(map);
        Records.instance().records.add(p);
        
        return "Success";
    }


    public String delete(Integer pid) {

        for (int i = 0; i < Records.instance().records.size(); i++) {

            Person p = Records.instance().records.get(i);

            if(visible(p)&&p.getpid().equals(pid)){

                if (rowLocked(p)){

                    return "Failure: Row is locked by another transaction";
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
            return "Database doesn't have any value";
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

            return "No such row";
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
        	output=output+p.getstr()+"\n\r\t   ";
        }
    	if(output.equals("")) return "No data";
    	else return output;
    }
    public boolean visible(Person p){

        if (Records.instance().active.contains(p.getcreated_tid())&& !p.getcreated_tid().equals(this.tid)){
            return false;
        }

        if (p.getexpired_tid().intValue() !=0 && (!Records.instance().active.contains(p.getexpired_tid())||p.getexpired_tid()==this.tid)){
            return false;
        }

        return true;

    }

    public boolean rowLocked(Person p){

        return p.getexpired_tid().intValue() !=0 && Records.instance().active.contains(p.getexpired_tid());

    }


    public ArrayList<Person> fetch(){
        
        ArrayList<Person> result = new ArrayList<>();


        for (Person p:Records.instance().records) {
            if (visible(p)){
                p.setLastRead_timestamp();
                result.add(p);
            }
            
        }
        Comparator<Person> c = new Comparator<Person>() {
            @Override
            public int compare(Person o1, Person o2) {
                if(o1.getpid()>o2.getpid()){
                    return 1;
                }
                return -1;
            }
        };

        result.sort(c);
        return result;

    }

    public String commit(){

    	if(this.rollback.isEmpty()) return "Failure: Nothing to commit";
        for (HashMap<String,String> action: this.rollback){
            int index = Integer.parseInt(action.get("order"));
            Person p = Records.instance().records.get(index);
            if (p.getLastRead_timestamp()>=p.getLastWrite_timestamp()){
                rollback();
                return "Commit fails, rollback automatically";
            }
        }
        Records.instance().active.remove(this.tid);
        return "Success";
    }


    public String rollback(){
    	if(this.rollback.isEmpty()) return "Failure: Nothing to rollback";
        Collections.reverse(this.rollback);
        for (HashMap<String,String> action: this.rollback) {
            if (action.get("action") == "add"){

                int index = Integer.parseInt(action.get("order"));
                Records.instance().records.get(index).setexpired_tid(0);
            }else if(action.get("action") == "delete"){
                int index = Integer.parseInt(action.get("order"));
                Records.instance().records.get(index).setexpired_tid(this.tid);
            }
        }

        Records.instance().active.remove(this.tid);
        return "Success";

    }

}
