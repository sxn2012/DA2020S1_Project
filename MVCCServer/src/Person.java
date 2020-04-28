/**
 * @Author: XIGUANG LI <xiguangl@student.unimelb.edu.au>
 * @Purpose: XIGUANGL
 **/


public class Person {
    
    private Integer pid;
    private String name;
    private Integer created_tid;
    private Integer expired_tid;
    Person(Integer pid,String name){
        this.pid = pid;
        this.name = name;
    }
    public void setpid(Integer pid) {
    	this.pid=pid;
    }
    public void setname(String name) {
    	this.name=name;
    }
    public void setcreated_tid(Integer created_tid) {
    	this.created_tid=created_tid;
    }
    public void setexpired_tid(Integer expired_tid) {
    	this.expired_tid=expired_tid;
    }
    public Integer getpid() {
    	return this.pid;
    }
    public String getname() {
    	return this.name;
    }
    public Integer getcreated_tid() {
    	return this.created_tid;
    }
    public Integer getexpired_tid() {
    	return this.expired_tid;
    }
    public String getstr() {
    	return "Person ID:"+pid.toString()+" Name:"+name;
    }
}
