package edu.buffalo.cse562;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;

import java.util.ArrayList;
import java.util.List;

public class OutputSchema {
    private List<SelectExpressionItem> colnames=new ArrayList<SelectExpressionItem>();
    public static CreateTable os;
    public static Table osTable;

    public OutputSchema(List<SelectExpressionItem> colnames){
        this.colnames=colnames;
        os=new CreateTable();
        osTable=new Table();
    }

    public void createOutputSchema() throws ParseException{
        List<Column> clist =new ArrayList<Column>();
        for(SelectExpressionItem e : colnames){
            if(e.getAlias()==null){
                
                Expression exp = e.getExpression();
                ExpressionEvaluator ev=new ExpressionEvaluator();
                exp.accept(ev);
                clist.add(ev.getColumn());
            
            }else{
                Column c=new Column();
                c.setTable(osTable);
                c.setColumnName(e.getAlias());
                clist.add(c);
            }
        }
        
        osTable.setName("osTable");
        os.setTable(osTable);
        os.setColumnDefinitions(clist);
    }
}