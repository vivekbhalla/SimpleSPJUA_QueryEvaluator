package edu.buffalo.cse562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SubSelect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class EvaluateStatement {
    @SuppressWarnings("unused")
    private FromItem from;
    @SuppressWarnings("unused")
    private Expression where;
 
    private List<Column> groupBy;
    private List<OrderByElement> orderby;
    private Select select;
    private List<Table> tables;
    private LinkedHashMap<Integer,List<Tuple>> outstream;

    public EvaluateStatement(Select select){
        this.select=select;
        this.from=null;
        this.where=null;
        this.groupBy=null;
        this.orderby=new ArrayList<OrderByElement>();
        this.tables=new ArrayList<Table>();
        this.outstream=new LinkedHashMap<Integer,List<Tuple>>();
    }

    @SuppressWarnings("unchecked")
    public LinkedHashMap<Integer,List<Tuple>> processQuery() throws ParseException{

        PlainSelect plain_select=(PlainSelect)select.getSelectBody();	
        SubSelect sub_select=null;
        
        FromItem from=plain_select.getFromItem();

        TablesNamesFinder t=new TablesNamesFinder();
        from.accept(t);

        
        
        tables=t.getTableList(select);
        sub_select=t.getSubSelect();
        List<SelectExpressionItem>selectList = new ArrayList<SelectExpressionItem>();
        selectList = plain_select.getSelectItems();
        new OutputSchema(selectList).createOutputSchema();

     
        
        
        if(sub_select!=null){
        	 Select s=new Select();
             s.setSelectBody(sub_select.getSelectBody());
        	outstream=new EvaluateStatement(s).processQuery();
        	tables=new ArrayList<Table>();
            Main.ct.add(OutputSchema.os);
       	
        }
        
        if(tables.size()==1){
        outstream=new OperatorFrom(tables,plain_select).caculateFrom();
        }else if(tables.size()>1){
        	outstream=new MultipleTable(tables,plain_select).caculateMultipleFrom();
        }
     
         
        groupBy=plain_select.getGroupByColumnReferences();

        if(groupBy!=null){
            outstream=new OperatorGroupBy(outstream,groupBy).calculateGroupBy();
        }
              
     

        int count=0;
        LinkedHashMap<Integer,List<Tuple>>new_outstream=new LinkedHashMap<Integer,List<Tuple>>();
        if(groupBy!=null){
        	
            for(Integer i : OperatorGroupBy.sel_op.keySet()){
                LinkedHashMap<Integer,List<Tuple>> instream = OperatorGroupBy.sel_op.get(i);
                new_outstream.put(count++,new OperatorProject(instream,plain_select).calculateGroupByProjection());
            }
            outstream=new_outstream;   
        }else{

            outstream=new OperatorProject(outstream,plain_select).calculateProjection();
        }
        

        orderby=plain_select.getOrderByElements();
     
        if(orderby!=null){
        	   Collections.reverse(orderby);
        	  
            for(OrderByElement o : orderby){
            	
                outstream=new OperatorOrderBy(outstream,o).calculateOrderBy();
            }
        }

 

        return outstream;
    }
}
