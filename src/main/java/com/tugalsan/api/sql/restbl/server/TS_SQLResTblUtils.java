package com.tugalsan.api.sql.restbl.server;

import java.util.*;
import java.util.stream.*;
import com.tugalsan.api.list.client.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.sql.resultset.server.*;
import com.tugalsan.api.sql.select.server.*;

public class TS_SQLResTblUtils {

    final private static TS_Log d = TS_Log.of(TS_SQLResTblUtils.class.getSimpleName());

    public static TGS_ListTable of(List list) {
        var t = new TS_SQLResTbl();
        if (list == null) {
            return t;
        }
        IntStream.range(0, list.size()).forEachOrdered(i -> t.setValue(i, list.get(i)));
        return t;
    }

    public static void fill(TS_SQLSelectExecutor executor, TGS_ListTable destTable) {
        fill(executor, destTable, false, null);
    }

    public static void fill(TS_SQLSelectExecutor executor, TGS_ListTable destTable, boolean addHeaders) {
        fill(executor, destTable, addHeaders, null);
    }

    public static void fill(TS_SQLSelectExecutor executor, TGS_ListTable destTable, List<String> useOptionalHeaders) {
        fill(executor, destTable, false, useOptionalHeaders);
    }

    private static void fill(TS_SQLSelectExecutor executor, TGS_ListTable destTable, boolean addHeaders, List<String> useOptionalHeaders) {
        executor.walk(rs -> fill(rs, destTable, addHeaders, useOptionalHeaders), rs -> fill(rs, destTable, addHeaders, useOptionalHeaders));
    }

    public static void fill(TS_SQLResultSet rs, TGS_ListTable destTable) {
        fill(rs, destTable, false, null);
    }

    public static void fill(TS_SQLResultSet rs, TGS_ListTable destTable, boolean addHeaders) {
        fill(rs, destTable, addHeaders, null);
    }

    public static void fill(TS_SQLResultSet rs, TGS_ListTable destTable, List<String> useOptionalHeaders) {
        fill(rs, destTable, false, useOptionalHeaders);
    }

    private static void fill(TS_SQLResultSet rs, TGS_ListTable destTable, boolean addHeaders, List<String> useOptionalHeaders) {
        destTable.clear();
        rs.walkCells(rs0 -> d.ci("fill", "empty", rs.meta.command()), (ri, ci) -> {
            destTable.setValue(ri, ci, rs.str.get(ri, ci));
        });
        if (useOptionalHeaders != null) {
            addHeaders = true;
        }
        if (!addHeaders) {
            return;
        }
        destTable.insertEmptyRow(0);
        IntStream.range(0, rs.col.size()).forEachOrdered(ci -> {
            destTable.setValue(0, ci, rs.col.name(ci));
        });
        if (useOptionalHeaders == null) {
            return;
        }
        IntStream.range(0, useOptionalHeaders.size()).forEachOrdered(ci -> {
            destTable.setValue(0, ci, useOptionalHeaders.get(ci));
        });
    }
}
