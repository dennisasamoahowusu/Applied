/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kgosafomaafo;

/**
 *
 * @author Dennis
 */
public class Tuple {

    Token token;
    String relation;
    public String[] selectedColumns = null;
    String[] conditionColumns = null;
    String[] conditionOperators = null;
    //contains the conditions. Matches the conditionColumns
    String[] conditions = null;
    String[] selectedRowIds = null;
    String[] selectedRows = null;

    public Tuple(Token token, String relation) {
        this.token = token;
        this.relation = relation;
    }

    public void setConditions(String[] conditionColumns, String[] conditions, String[] conditionOperators) {
        if (conditionColumns.length == conditions.length &&
                conditions.length == conditionOperators.length) {
            this.conditionColumns = new String[conditionColumns.length];
            this.conditions = new String[conditions.length];
            this.conditionOperators = new String[conditionOperators.length];
            System.arraycopy(conditionColumns, 0, this.conditionColumns, 0, conditionColumns.length);
            System.arraycopy(conditions, 0, this.conditions, 0, conditions.length);
            System.arraycopy(conditionOperators, 0, this.conditionOperators, 0, conditionOperators.length);
        }
    }

    public void setSelectedColumns(String[] selectedColumns) {
        if (selectedColumns != null && selectedColumns.length > 0) {
            this.selectedColumns = new String[selectedColumns.length];
            System.arraycopy(selectedColumns, 0, this.selectedColumns, 0, selectedColumns.length);
        }
    }

    //gives translation of selected attributes
    public String getTranslation1() {
        String columns = "";
        if (this.selectedColumns != null) {
            if (this.selectedColumns.length > 0) {
                columns += this.selectedColumns[0];
                for (int i = 1; i < this.selectedColumns.length; i++) {
                    columns += "," + this.selectedColumns[i];
                }
            }
        }
        if (columns.equals("")) {
            columns = "all columns";
        }
        return columns;
    }

    //gives translation of conditions
    public String getTranslation2() {
        String translation = "";
        if (this.conditionColumns != null) {
            if (this.conditionColumns.length > 0) {
                translation += this.conditionColumns[0] + this.conditionOperators[0] +
                        this.conditions[0];
            }
            for (int i=1; i < this.conditionColumns.length; i++) {
                translation += " and " + this.conditionColumns[i] + this.conditionOperators[i] +
                        this.conditions[i];
            }
        }
        return translation;
    }

}
