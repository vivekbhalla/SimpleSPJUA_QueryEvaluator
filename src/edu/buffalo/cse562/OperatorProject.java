package edu.buffalo.cse562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;



public class OperatorProject {
	private LinkedHashMap<Integer,List<Tuple>> instream;
	private PlainSelect select;
	private LinkedHashMap<Integer,List<Tuple>> outstream;
	private List<SelectExpressionItem> selectList;
	public List<Expression> colnames;


	public OperatorProject(LinkedHashMap<Integer,List<Tuple>> instream,PlainSelect select){
		this.instream=instream;
		this.select=select;
		this.outstream=new LinkedHashMap<Integer,List<Tuple>>();
		this.selectList = new ArrayList<SelectExpressionItem>();
		this.colnames = new ArrayList<Expression>();
	}
	@SuppressWarnings("unchecked")

	public LinkedHashMap<Integer,List<Tuple>> calculateProjection() throws ParseException{

		List<Tuple> new_tlist;
		StringBuilder sb;
		selectList = select.getSelectItems();
		List <String> colList=new LinkedList<String>();
		for(SelectExpressionItem s : selectList ){

			Expression exp=s.getExpression();
			ExpressionEvaluator ee=new ExpressionEvaluator();
			exp.accept(ee);
			if(ee.getUserColumn()!=null){
				colList.add(ee.getUserColumn().getColumnName().toString());
			}else
				colList.add(ee.getColumn().getColumnName().toString());
		}  


		for(String col : colList){

			if(col.contains("|")){
				new_tlist=calculateGroupByProjection(); 
				outstream.put(0,new_tlist);
				return outstream;			
			}

		}
		int count =0;
		for(Integer i : instream.keySet()){
			sb=new StringBuilder();
			new_tlist=new ArrayList<Tuple>();
			for(String col : colList){
				for(Tuple t : instream.get(i)){
					if(col.contains("(")){
						long val=calculate(t,col);
						sb.append(val+"|");
						break;
					}
					else{
						String val=t.getColValue(col);
						if(val!=null){
							sb.append(val+"|");
							break;
						}
					} 
				}
			}
		

		String t=sb.substring(0,sb.length()-1);
		Tuple tuple=new Tuple(OutputSchema.osTable,t);
		new_tlist.add(tuple);

		outstream.put(count++, new_tlist);

	}

	return outstream;
}


@SuppressWarnings("unchecked")
public List<Tuple> calculateGroupByProjection() throws ParseException{
	List<Tuple> new_tlist=new ArrayList<Tuple>();
	StringBuilder sb=new StringBuilder();

	selectList = select.getSelectItems();
	List<Tuple> tlist=instream.get(0);

	for(SelectExpressionItem s : selectList ){

		String col=new String();
		Expression exp=s.getExpression();

		ExpressionEvaluator ee=new ExpressionEvaluator();
		exp.accept(ee);

		if(ee.getUserColumn()!=null)
			col=ee.getUserColumn().getColumnName().toString();
		else
			col=ee.getColumn().getColumnName().toString();

		if(col.contains("|")){

			String fnct=col.split("\\|")[0];
			String expn=col.split("\\|")[1];
			if(fnct.equalsIgnoreCase("COUNT")){

				sb.append(new Count(instream,expn).getCount()+"|");

			}else if(fnct.equalsIgnoreCase("SUM")){
				sb.append(new Sum(instream,expn).getSum()+"|");

			}else if(fnct.equalsIgnoreCase("AVG")){
				sb.append(new Avg(instream,expn).getAvg()+"|");
			}

		}else{

			for(Tuple t : tlist){
				String val=t.getColValue(col);
				if(val!=null){
					sb.append(val+"|");
					break;
				}
			}
		}

	}
	String t=sb.substring(0,sb.length()-1);
	Tuple tuple=new Tuple(OutputSchema.osTable,t);
	new_tlist.add(tuple);

	return new_tlist;
}
public String createExpression(Tuple t,String condition,LinkedHashSet<String> colnames)
{

	for(String s : colnames){

		String val=t.getColValue(s);

		if(val!=null){
			if(checkString(val))
				condition=condition.replace(s,"'"+val+"'");
			else{
			
				condition=condition.replace(s,val);
			} 
		}


	}


	return condition;
}

boolean checkString(String s){ 
	try{
		Integer.parseInt(s);
		return false;
	}catch(Exception e){
		try{
			Double.parseDouble(s);
			return false;
		}
		catch(Exception e1){
			return true;
		}
	}
} 

public long calculate(Tuple t,String col) throws ParseException{

	   		
	Expression exp4 = Main.parseArithematicExpression(col);
	ExpressionEvaluator eee1=new ExpressionEvaluator();
	exp4.accept(eee1);
	LinkedHashSet<String> colnames=eee1.getColNames();
	
	

	String condition=createExpression(t,col,colnames);    		
	Expression exp5 = Main.parseArithematicExpression(condition);
	ExpressionEvaluator ee1=new ExpressionEvaluator();
	exp5.accept(ee1);

	long l=ee1.getLongEvaluation();
	return l;
}

}