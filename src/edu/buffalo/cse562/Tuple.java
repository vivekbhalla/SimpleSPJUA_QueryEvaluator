package edu.buffalo.cse562;

import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.create.table.CreateTable;


public class Tuple {
    private Table table;
    private String[] tuple;

    public Tuple(Table table,String tuple){
        this.tuple=tuple.split("\\|");
        this.table=table;
    }

    public String[] getTupleVaule(){
        return tuple;
    }
    public String getTableName(){
        return table.getWholeTableName();
    }
    public String getAlias(){
        if(table.getAlias() != null)
            return table.getAlias();
        else
            return table.getWholeTableName();
    }

    public String getColValue(String col){
    	
        int count=0;
        String alias=new String();
        if(col.contains(".")){
       
            alias=col.split("\\.")[0];
            col=col.split("\\.")[1];
            if(!this.getAlias().equalsIgnoreCase(alias)){
            	return null;
            }
                
        }

        for(CreateTable ct : Main.ct){

            String t1=ct.getTable().toString();
            String t2=table.getWholeTableName().toString();

            if(t1.equalsIgnoreCase(t2)){

                for(Object s : ct.getColumnDefinitions()){
                    String col_defn=s.toString();
                  
                    if(col_defn.contains(col)){

                        return tuple[count];
                    }
                    count++;
                }
            }
        }
        return null;
    }

    public String getOsColValue(String col){
        int count=0;
        if(col.contains(".")){
            col=col.split("\\.")[1];
        }


        CreateTable ct=OutputSchema.os;

        String t1=ct.getTable().toString();
        String t2=table.getWholeTableName().toString();

        if(t1.equalsIgnoreCase(t2)){

            for(Object s : ct.getColumnDefinitions()){
                String col_defn=s.toString();

                if(col_defn.contains(col)){

                    return tuple[count];
                }
                count++;
            }
        }

        return null;
    }

public void printTuple(){
	for(String s : this.tuple){
		System.out.print(s+"|");
	}
	System.out.println();
}
}

