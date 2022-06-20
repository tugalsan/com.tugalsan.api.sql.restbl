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
        executor.walk(rs -> d.ci("fill", ()-> rs.meta.command()), rs -> fill(rs, destTable, addHeaders, useOptionalHeaders));
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
        if (useOptionalHeaders != null) {
            addHeaders = true;
        }
        destTable.clear();
        if (rs.row.isEmpty()) {
            return;
        }
        IntStream.range(0, rs.row.size()).forEachOrdered(ri -> {
            IntStream.range(0, rs.col.size()).forEachOrdered(ci -> {
                destTable.setValue(ri, ci, rs.str.get(ri, ci));
            });
        });
        if (addHeaders) {
            List<String> finalHeaders = TGS_ListUtils.of();
            if (useOptionalHeaders == null) {
                IntStream.range(0, rs.col.size()).forEachOrdered(ci -> {
                    finalHeaders.add(rs.col.name(ci));
                });
            } else {
                IntStream.range(0, useOptionalHeaders.size()).forEachOrdered(ci -> {
                    finalHeaders.add(useOptionalHeaders.get(ci));
                });
            }
            destTable.insertEmptyRow(0);
            IntStream.range(0, finalHeaders.size()).forEachOrdered(ci -> {
                destTable.setValue(0, ci, finalHeaders.get(ci));
            });
        }
    }
}
