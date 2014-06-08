package edu.buffalo.cse562;


import net.sf.jsqlparser.statement.StatementVisitor;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.drop.Drop;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;

public class StatementEvaluator implements StatementVisitor {

    public void visit(CreateTable ct) {
        Main.ct.add(ct);
    }

    public void visit(Select sel) {
        Main.select.add(sel);

    }

    public void visit(Delete del) {

        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public void visit(Drop dp) {

        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public void visit(Insert in) {

        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public void visit(Replace rp) {

        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public void visit(Truncate tr) {

        throw new UnsupportedOperationException("Not supported yet."); 
    }

    public void visit(Update up) {

        throw new UnsupportedOperationException("Not supported yet."); 
    }


}


