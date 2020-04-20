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
}
