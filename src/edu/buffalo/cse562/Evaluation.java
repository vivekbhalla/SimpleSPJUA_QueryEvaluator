
package edu.buffalo.cse562;

import net.sf.jsqlparser.parser.ParseException;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Evaluation {
    public static Queue<PlainSelect> plain_selects = new LinkedList<PlainSelect>();
    private LinkedHashMap<Integer, List<Tuple>> outstream;

    public void evaluate_sql() throws ParseException
    {

        for (Select s : Main.select) {

            SelectEvaluator e = new SelectEvaluator();
            s.getSelectBody().accept(e);
            if (plain_selects.size() == 1) {
                outstream = new EvaluateStatement(s).processQuery();

                for (Integer i : outstream.keySet()) {
                    for (Tuple t1 : outstream.get(i)) {
                        String s1[] = t1.getTupleVaule();
                        for (int c = 0; c < s1.length - 1; c++) {
                            System.out.print(s1[c] + "|");
                        }
                        System.out.print(s1[s1.length - 1]);
                    }
                    System.out.println();
                }

            }
            else {
                // to implement union between different selects
            }
            plain_selects = new LinkedList<PlainSelect>();
        }
    }
}
