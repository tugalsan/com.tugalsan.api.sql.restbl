package com.tugalsan.api.sql.restbl.server;

import java.util.*;
import java.util.stream.*;
import com.tugalsan.api.list.client.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.sql.resultset.server.*;
import com.tugalsan.api.sql.select.server.*;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;

public class TS_SQLResTblUtils {

    final private static TS_Log d = TS_Log.of(TS_SQLResTblUtils.class);

    public static TGS_ListTable of(List list) {
        var t = TS_SQLResTbl.of();
        if (list == null) {
            return t;
        }
        IntStream.range(0, list.size()).forEachOrdered(i -> t.setValues(i, list.get(i)));
        return t;
    }

    public static void fill(TS_ThreadSyncTrigger servletKillTrigger, TS_SQLSelectExecutor executor, TGS_ListTable destTable) {
        fill(servletKillTrigger, executor, destTable, false, null);
    }

    public static void fill(TS_ThreadSyncTrigger servletKillTrigger, TS_SQLSelectExecutor executor, TGS_ListTable destTable, boolean addHeaders) {
        fill(servletKillTrigger, executor, destTable, addHeaders, null);
    }

    public static void fill(TS_ThreadSyncTrigger servletKillTrigger, TS_SQLSelectExecutor executor, TGS_ListTable destTable, List<String> useOptionalHeaders) {
        fill(servletKillTrigger, executor, destTable, false, useOptionalHeaders);
    }

    private static void fill(TS_ThreadSyncTrigger servletKillTrigger, TS_SQLSelectExecutor executor, TGS_ListTable destTable, boolean addHeaders, List<String> useOptionalHeaders) {
        executor.walk(rs -> fill(servletKillTrigger, rs, destTable, addHeaders, useOptionalHeaders), rs -> fill(servletKillTrigger, rs, destTable, addHeaders, useOptionalHeaders));
    }

    public static void fill(TS_ThreadSyncTrigger servletKillTrigger, TS_SQLResultSet rs, TGS_ListTable destTable) {
        fill(servletKillTrigger, rs, destTable, false, null);
    }

    public static void fill(TS_ThreadSyncTrigger servletKillTrigger, TS_SQLResultSet rs, TGS_ListTable destTable, boolean addHeaders) {
        fill(servletKillTrigger, rs, destTable, addHeaders, null);
    }

    public static void fill(TS_ThreadSyncTrigger servletKillTrigger, TS_SQLResultSet rs, TGS_ListTable destTable, List<String> useOptionalHeaders) {
        fill(servletKillTrigger, rs, destTable, false, useOptionalHeaders);
    }

    private static void fill(TS_ThreadSyncTrigger servletKillTrigger, TS_SQLResultSet rs, TGS_ListTable destTable, boolean addHeaders, List<String> useOptionalHeaders) {
        if (servletKillTrigger.hasTriggered()) {
            return;
        }
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
