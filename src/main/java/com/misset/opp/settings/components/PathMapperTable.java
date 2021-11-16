package com.misset.opp.settings.components;

import com.intellij.execution.util.ListTableWithButtons;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.ColumnInfo;
import com.intellij.util.ui.ListTableModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PathMapperTable extends ListTableWithButtons<PathMapperTable.Item> {

    public PathMapperTable() {
        JBTable table = getTableView();
        table.getEmptyText().clear();
        table.getEmptyText().appendLine("Add mappings to resolve OMT imports that use paths");
        table.getEmptyText().appendLine("examples: @client, @activiteiten etc...");
        table.setStriped(false);
    }

    @Override
    protected ListTableModel createListModel() {
        return new ListTableModel<Item>(NAME, PATH);
    }

    @Override
    protected Item createElement() {
        return new Item("", "");
    }

    @Override
    protected boolean isEmpty(Item element) {
        return element.name == null || element.name.isEmpty();
    }

    @Override
    protected Item cloneElement(Item variable) {
        return new Item(variable.name, variable.path);
    }

    @Override
    protected boolean canDeleteElement(Item selection) {
        return true;
    }

    private static final ColumnInfo<Item, String> NAME = new ColumnInfo<>("Name") {
        @Override
        public @Nullable String valueOf(Item item) {
            return item.name;
        }

        @Override
        public boolean isCellEditable(Item item) {
            return true;
        }

        @Override
        public void setValue(Item item,
                             String value) {
            item.name = value;
        }
    };

    private static final ColumnInfo<Item, String> PATH = new ColumnInfo<>("Path") {
        @Override
        public @Nullable String valueOf(Item item) {
            return item.path;
        }

        @Override
        public boolean isCellEditable(Item item) {
            return true;
        }

        @Override
        public void setValue(Item item,
                             String value) {
            item.path = value;
        }
    };

    public static class Item {
        String name;
        String path;

        public Item(@NotNull String name,
                    @NotNull String path) {
            this.name = name;
            this.path = path;
        }

        public String getName() {
            return name;
        }

        public String getPath() {
            return path;
        }
    }

}
