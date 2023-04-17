package com.tugalsan.api.sql.restbl.server;

import java.util.*;
import java.util.stream.*;
import com.tugalsan.api.cast.server.*;
import com.tugalsan.api.list.client.*;
import com.tugalsan.api.time.client.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.unsafe.client.*;

public class TS_SQLResTbl extends TGS_ListTable {

    final private static TS_Log d = TS_Log.of(TS_SQLResTbl.class);

    public static TS_SQLResTbl of() {
        return new TS_SQLResTbl();
    }

    public static TS_SQLResTbl of(List list) {
        var t = new TS_SQLResTbl();
        IntStream.range(0, list.size()).forEachOrdered(ri -> {
            t.setValue(ri, 0, list.get(ri));
        });
        return t;
    }

    @Override
    public TS_SQLResTbl cloneIt() {
        var clone = new TS_SQLResTbl();
        clone.sniffRows(this);
        clone.setHeaderBold(super.isHeaderBold());
        return clone;
    }

    @Override
    public void setValue(int rowIndex, int columnIndex, Object value) {
        while (rowIndex >= rows.size()) {
            rows.add(TGS_ListUtils.of());
        }
        var row = (List) rows.get(rowIndex);
        while (columnIndex >= row.size()) {
            row.add("");
        }
        if (value != null || value instanceof TS_SQLResTblValue) {
            row.set(columnIndex, value);
        } else {
            super.setValue(rowIndex, columnIndex, value);
        }
    }

    public boolean isSQLValue(int rowIndex, int columnIndex) {
        return getValueAsObject(rowIndex, columnIndex) instanceof TS_SQLResTblValue;
    }

    public TS_SQLResTblValue getValueAsSQLValue(int rowIndex, int columnIndex) {
        var r = getValueAsObject(rowIndex, columnIndex);
        return r != null && r instanceof TS_SQLResTblValue ? (TS_SQLResTblValue) r : null;
    }

    // COLUMNWISE ---------------------------------------------------------------------------------
    public void cast2SQLColumn(int colIdx, CharSequence tablename_dot_columnname, int fromColIdx, Integer skipHeaderCount) {
        var rowSize = rows.size();
        int cs;
        for (var ri = skipHeaderCount == null ? 0 : skipHeaderCount; ri < rowSize; ri++) {
            cs = getColumnSize(ri);
            if (cs > colIdx) {
                d.ci("cast2SQLColumn.ri:", ri, ", colIdx:", colIdx);
                setSQLValue(ri, colIdx, tablename_dot_columnname, fromColIdx);
            }
        }
    }

    public void cast2TimeColumn(int colIdx, int fromColIdx, Integer skipHeaderCount) {
        var rowSize = rows.size();
        int cs;
        for (var ri = skipHeaderCount == null ? 0 : skipHeaderCount; ri < rowSize; ri++) {
            cs = getColumnSize(ri);
            if (cs > colIdx) {
                d.ci("cast2TimeColumn.ri:", ri, ", colIdx:", colIdx, ", fromColIdx:", fromColIdx);
                setTimeValue(ri, colIdx, fromColIdx);
            }
        }
    }

    public void cast2DateColumn(int colIdx, int fromColIdx, Integer skipHeaderCount) {
        int rowSize = rows.size();
        int cs;
        for (var ri = skipHeaderCount == null ? 0 : skipHeaderCount; ri < rowSize; ri++) {
            cs = getColumnSize(ri);
            if (cs > colIdx) {
                d.ci("cast2DateColumn.ri:", ri, ", colIdx:", colIdx, ", fromColIdx:", fromColIdx);
                setDateValue(ri, colIdx, fromColIdx);
            }
        }
    }

    // SETTERS---------------------------------------------------------------------------------------------
    public void setSQLValue(int rowIdx, int colIdx, CharSequence tablename_dot_columnname, CharSequence idStr) {
        var cellSQL = new TS_SQLResTblValue(tablename_dot_columnname.toString(), idStr.toString());
        setValue(rowIdx, colIdx, cellSQL);
    }

    public void setSQLValue(int rowIdx, int colIdx, CharSequence tablename_dot_columnname, int fromColIdx) {//fromColIdx = id
        d.ci("cast2SQLValue -> rowIdx: ", rowIdx, ", colIdx:", colIdx, ", tablename_dot_columnname:", tablename_dot_columnname, ", idAtColIdx: ", fromColIdx);
        var idObj = getValueAsObject(rowIdx, fromColIdx);
        if (idObj == null) {
            TGS_UnSafe.thrw(d.className, "setSQLValue", "idObj == null");
        }
        if (idObj instanceof TS_SQLResTblValue val) {
            d.ce("cast2SQLValue.val.getTableAndColumnName:", val.tableAndColumnName);
            d.ce("cast2SQLValue.val.getId: ", val.id);
            TGS_UnSafe.thrw(d.className, "setSQLValue", "idObj instanceof TS_SQLResTblValue val");
        }
        var idStr = String.valueOf(idObj);
        var cellSQL = new TS_SQLResTblValue(tablename_dot_columnname.toString(), idStr);
        setValue(rowIdx, colIdx, cellSQL);
    }

    public void setDateValue(int rowIdx, int colIdx, int fromColIdx) {
        var valO = getValueAsObject(rowIdx, fromColIdx);
        if (valO == null) {
            TGS_UnSafe.thrw(d.className, "setDateValue", "valO == null");
        }
        var valStr = String.valueOf(valO);
        var valLng = Long.valueOf(valStr);
        var dateStr = TGS_Time.toString_dateOnly(valLng);
        setValue(rowIdx, colIdx, dateStr);
    }

    public void setTimeValue(int rowIdx, int colIdx, int fromColIdx) {
        var valO = getValueAsObject(rowIdx, fromColIdx);
        if (valO == null) {
            TGS_UnSafe.thrw(d.className, "setTimeValue", "valO == null");
        }
        var valStr = String.valueOf(valO);
        var valLng = Long.valueOf(valStr);
        var timeStr = TGS_Time.toString_timeOnly_simplified(valLng);
        setValue(rowIdx, colIdx, timeStr);
    }

    public void setValuePrecision(int rowIndex, int columnIndex, int precision) {
        var val = getValueAsDouble(rowIndex, columnIndex);
        if (val == null) {
            return;
        }
        var str = TS_CastUtils.toString(val, precision);
        setValue(rowIndex, columnIndex, str);
    }

    public void setColumnPrecision(int columnIndex, int precision, int skipHeaderRowCount) {
        IntStream.range(skipHeaderRowCount, getRowSize()).forEachOrdered(ri -> {
            setValuePrecision(ri, columnIndex, precision);
        });
    }

    public void setValue(int rowIndex, int columnIndex, Double value, int precision) {
        setValue(rowIndex, columnIndex, value);
        setValuePrecision(rowIndex, columnIndex, precision);
    }
}
