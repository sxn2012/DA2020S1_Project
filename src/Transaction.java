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

    public void add(Person p){
        p.created_tid = this.tid;
        p.expired_tid = 0;
        HashMap<String,String> map = new HashMap<>(){
            {
                put("action","delete");
                put("order",String.valueOf(Records.instance().records.size()));
            }
        };
        this.rollback.add(map);
        Records.instance().records.add(p);


    }


    public void delete(Integer pid) throws Exception{

        for (int i = 0; i < Records.instance().records.size(); i++) {

            Person p = Records.instance().records.get(i);

            if(visible(p)&&p.pid == pid){

                if (rowLocked(p)){

                    throw new Exception("Row is locked by another transaction");
                }else{

                    p.expired_tid = this.tid;
                    HashMap<String,String> map = new HashMap<>();
                    map.put("action","add");
                    map.put("order",String.valueOf(i));

                    this.rollback.add(map);

                }
            }

        }

    }


    public void update(Integer pid,String name){
         try {
             delete(pid);
             add(new Person(pid,name));

         }catch (Exception e){

             System.out.println(e.toString());
         }


    }


    public boolean visible(Person p){

        if (Records.instance().active.contains(p.created_tid)&& p.created_tid!=this.tid){
            return false;
        }

        if (p.expired_tid !=0 && (!Records.instance().active.contains(p.expired_tid)||p.expired_tid==this.tid)){
            return false;
        }

        return true;

    }

    public boolean rowLocked(Person p){

        return p.expired_tid !=0 && Records.instance().active.contains(p.expired_tid);

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

    public void commit(){

        Records.instance().active.remove(this.tid);
    }


    public void rollback(){

        Collections.reverse(this.rollback);
        for (HashMap<String,String> action: this.rollback) {
            if (action.get("action") == "add"){

                int index = Integer.parseInt(action.get("order"));
                Records.instance().records.get(index).expired_tid = 0;
            }else if(action.get("action") == "delete"){
                int index = Integer.parseInt(action.get("order"));
                Records.instance().records.get(index).expired_tid = this.tid;
            }
        }

        Records.instance().active.remove(this.tid);


    }

}
