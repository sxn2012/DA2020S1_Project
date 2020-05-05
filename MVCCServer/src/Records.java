import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * @Author: XIGUANG LI <xiguangl@student.unimelb.edu.au>
 * @Purpose: XIGUANGL
 **/
public class Records {


    ArrayList<Person> records;
    HashSet<Integer> active;


    private Records(){

        records = new ArrayList<>();
        active = new HashSet<>();

    }

    public static Records instance(){
        return instanceHolder.instance;
    }

    private static class instanceHolder {
        private static final Records instance = new Records();
    }

    public static synchronized int addItemToRecords(Person p){

        instance().records.add(p);
        return instance().records.size()-1;

    }

    public static void garbageClean(){

        ArrayList<Person> t = new ArrayList<>(instance().records);
        ArrayList<Person> cleanedRecord = new ArrayList<>();

        for (Person p : t) {
            if (p.getexpired_tid() == 0 || (p.getexpired_tid() != 0 && instance().active.contains(p.getexpired_tid()))) {
                cleanedRecord.add(p);
            }

        }
        
        instance().records = cleanedRecord;




    }



}
