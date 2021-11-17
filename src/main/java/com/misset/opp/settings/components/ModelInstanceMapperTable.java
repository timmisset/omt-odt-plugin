package com.misset.opp.settings.components;

import com.intellij.execution.util.ListTableWithButtons;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ModelInstanceMapperTable extends ListTableWithButtons<ModelInstanceMapperTable.Item> {

    public ModelInstanceMapperTable() {
        JBTable table = getTableView();
        table.getEmptyText().clear();
        table.setStriped(false);
    }

    @Override
    protected ListTableModel createListModel() {
        return new ListTableModel<Item>(CLASS, REGEX);
    }

    @Override
    protected Item createElement() {
        return new Item("", "");
    }

    @Override
    protected boolean isEmpty(Item element) {
        return element.regex == null || element.ontologyClass.isEmpty();
    }

    @Override
    protected Item cloneElement(Item variable) {
        return new Item(variable.regex, variable.ontologyClass);
    }

    @Override
    protected boolean canDeleteElement(Item selection) {
        return true;
    }

    private static final ColumnInfo<Item, String> REGEX = new ColumnInfo<>("RegEx") {
        @Override
        public @Nullable String valueOf(Item item) {
            return item.regex;
        }

        @Override
        public boolean isCellEditable(Item item) {
            return true;
        }

        @Override
        public void setValue(Item item,
                             String value) {
            item.regex = value;
        }
    };

    private static final ColumnInfo<Item, String> CLASS = new ColumnInfo<>("Ontology Class") {
        @Override
        public @Nullable String valueOf(Item item) {
            return item.ontologyClass;
        }

        @Override
        public boolean isCellEditable(Item item) {
            return true;
        }

        @Override
        public void setValue(Item item,
                             String value) {
            item.ontologyClass = value;
        }
    };

    public static class Item {
        String regex;
        String ontologyClass;

        public Item(@NotNull String regex,
                    @NotNull String ontologyClass) {
            this.regex = regex;
            this.ontologyClass = ontologyClass;
        }

        public String getRegEx() {
            return regex;
        }

        public String getOntologyClass() {
            return ontologyClass;
        }
    }

}
