import java.util.ArrayList;
import java.util.Collections;
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

            if(visible(p)&&p.getpid() == pid){

                if (rowLocked(p)){

                    return "Failure: Row is locked by another transaction";
                }else{

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

        if (Records.instance().active.contains(p.getcreated_tid())&& p.getcreated_tid()!=this.tid){
            return false;
        }

        if (p.getexpired_tid() !=0 && (!Records.instance().active.contains(p.getexpired_tid())||p.getexpired_tid()==this.tid)){
            return false;
        }

        return true;

    }

    public boolean rowLocked(Person p){

        return p.getexpired_tid() !=0 && Records.instance().active.contains(p.getexpired_tid());

    }


    public ArrayList<Person> fetch(){
        
        ArrayList<Person> result = new ArrayList<>();

        for (Person p:Records.instance().records) {
            if (visible(p)){
                result.add(p);
            }
            
        }

        return result;

    }

    public String commit(){

        Records.instance().active.remove(this.tid);
        return "Success";
    }


    public String rollback(){

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
