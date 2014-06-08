package edu.buffalo.cse562;


import net.sf.jsqlparser.parser.ParseException;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;

public class Avg {
    private LinkedHashMap<Integer,List<Tuple>> instream;
    private String exp;


    public Avg(LinkedHashMap<Integer,List<Tuple>> instream, String exp) {
        this.instream=instream;
        this.exp=exp;
    }

    public String getAvg() throws ParseException{
        if(instream!=null){
        	
        	String s=new Sum(instream,exp).getSum();
       
        	double sum=Double.parseDouble(s);
        	
            double avg=sum/(new Count(instream,exp).getCount());
			 DecimalFormat f = new DecimalFormat("##0.0##");
             return (f.format(avg).toString());
            
        }else
            return null;

    }

}
