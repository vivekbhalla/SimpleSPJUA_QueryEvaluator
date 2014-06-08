package edu.buffalo.cse562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.ParseException;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;

public class Sum {
	private LinkedHashMap<Integer,List<Tuple>> instream;
	private String exp;
	private LinkedHashSet<String>colList;

	public Sum(LinkedHashMap<Integer,List<Tuple>> instream, String exp) {
		this.instream=instream;
		this.exp=exp;
		this.colList=new LinkedHashSet<String>();
	}

	public String getSum() throws ParseException{



		Expression exp2=Main.parseArithematicExpression(exp);
		ExpressionEvaluator e2=new ExpressionEvaluator();
		exp2.accept(e2);
		colList=e2.getColNames();
		Double sum=new Double(0);	
		for(List<Tuple> l: instream.values()){
				String condition=createExpression(l);
				Expression exp3=Main.parseArithematicExpression(condition);
				ExpressionEvaluator e3=new ExpressionEvaluator();
				exp3.accept(e3);
				sum=sum+e3.getDoulbeEvaluation();
			}
		
   	        	 DecimalFormat f = new DecimalFormat("##0.0##");
                return (f.format(sum).toString());
	}	
	public String createExpression(List<Tuple> instream)
	{

		String condition=exp;
		for(String s : colList){
			for(Tuple t : instream){
				String val=t.getColValue(s);
				if(val!=null){
					Double d=Double.parseDouble(val);
					condition=condition.replace(s,d.toString());
				}
			}
		}
		return condition;
	}
}