import java.lang.reflect.Array;
import java.util.HashSet;

public class Main {

    static Integer transaction_id = 1;


    public static void main(String[] args) {

        Transaction one = newTransaction();
        one.add(new Person(1,"name1"));
        one.add(new Person(2,"name2"));
//        one.commit();

//        Transaction three = newTransaction();
        Transaction two = newTransaction();



        System.out.println("Before:");
        System.out.println("transaction one:"+one.fetch());
        System.out.println("transaction two:"+two.fetch());

//        three.update(1,"name3");

//       one.rollback();
        one.commit();
        System.out.println("After:");
        System.out.println("transaction one:"+one.fetch());
        System.out.println("transaction two:"+two.fetch());





    }


    public static Transaction newTransaction(){

        transaction_id++;
        Records.instance().active.add(transaction_id);

        return new Transaction(transaction_id);
    }


}
