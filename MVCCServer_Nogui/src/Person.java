import java.util.Date;

/**
 * @Author: XIGUANG LI <xiguangl@student.unimelb.edu.au>
 * @Purpose: XIGUANGL
 **/


public class Person {
    
    private Integer pid;
    private String name;
    private Integer created_tid;
    private Integer expired_tid;
    private long lastWrite_timestamp;
    private long lastRead_timestamp;
    Person(Integer pid,String name){
        this.pid = pid;
        this.name = name;
        this.lastRead_timestamp = new Date().getTime();
        this.lastWrite_timestamp = this.lastRead_timestamp+1;

    }
    Person(Integer pid,String name,Integer created_tid,Integer expired_tid,long lastWrite_timestamp,long lastRead_timestamp){
        this.pid = pid;
        this.name = name;
        this.created_tid=created_tid;
        this.expired_tid=expired_tid;
        this.lastRead_timestamp=lastRead_timestamp;
        this.lastWrite_timestamp=lastWrite_timestamp;
    }
    public void setLastWrite_timestamp(){ this.lastWrite_timestamp = new Date().getTime(); }
    public void setLastRead_timestamp(){ this.lastRead_timestamp = new Date().getTime();}
    public long getLastWrite_timestamp(){return this.lastWrite_timestamp;}
    public long getLastRead_timestamp(){return this.lastRead_timestamp;}
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
    	return "Person ID:"+pid.toString()+" Name:"+name + " LastRead:" + this.lastRead_timestamp;
    }
}
