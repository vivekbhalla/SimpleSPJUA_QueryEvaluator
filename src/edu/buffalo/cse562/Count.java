package edu.buffalo.cse562;


import java.util.LinkedHashMap;
import java.util.List;

public class Count {
    private LinkedHashMap<Integer,List<Tuple>> instream;
    @SuppressWarnings("unused")
    private String exp;


    public Count(LinkedHashMap<Integer,List<Tuple>> instream, String exp) {
        this.instream=instream;
        this.exp=exp;
    }

    public int getCount(){
        if(instream!=null){
            return instream.size();
        }else
            return 0;

    }

}
