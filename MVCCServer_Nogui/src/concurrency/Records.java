package concurrency;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * @Author: XIGUANG LI <xiguangl@student.unimelb.edu.au>
 * @Purpose: XIGUANGL
 **/
public class Records {


	private ArrayList<Person> records;
	private HashSet<Long> active;


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

    public static synchronized int addItemToRecords(Person p,ArrayList<Person> cleanedRecord){
        if (p == null){
            instance().records = cleanedRecord;
            return 0;
        }else {
            instance().records.add(p);
            return instance().records.size() - 1;
        }


    }


    public static void garbageClean(){

        if(!instance().active.isEmpty()){

            return;
        }
        int initialCount = instance().records.size();
        ArrayList<Person> t = new ArrayList<>(instance().records);
        ArrayList<Person> cleanedRecord = new ArrayList<>();

        for (Person p : t) {
            if (p.getexpired_tid() == 0 || (p.getexpired_tid() != 0 && instance().active.contains(p.getexpired_tid()))) {
                cleanedRecord.add(p);
            }

        }

        if (initialCount == instance().records.size()){
            return;
        }
        addItemToRecords(null,cleanedRecord);

    }

	public ArrayList<Person> getRecords() {
		return records;
	}

	public void setRecords(ArrayList<Person> records) {
		this.records = records;
	}

	public HashSet<Long> getActive() {
		return active;
	}

	public void setActive(HashSet<Long> active) {
		this.active = active;
	}



}
