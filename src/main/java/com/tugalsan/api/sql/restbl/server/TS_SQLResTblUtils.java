package com.tugalsan.api.sql.restbl.server;

import java.util.*;
import java.util.stream.*;
import com.tugalsan.api.list.client.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.sql.resultset.server.*;
import com.tugalsan.api.sql.select.server.*;
import com.tugalsan.api.union.client.TGS_UnionExcuse;
import com.tugalsan.api.union.client.TGS_UnionExcuseVoid;

public class TS_SQLResTblUtils {

    final private static TS_Log d = TS_Log.of(TS_SQLResTblUtils.class);

    public static TGS_ListTable of(List list) {
        var t = TS_SQLResTbl.of();
        if (list == null) {
            return t;
        }
        IntStream.range(0, list.size()).forEachOrdered(i -> t.setValue(i, list.get(i)));
        return t;
    }

    public static TGS_UnionExcuseVoid fill(TS_SQLSelectExecutor executor, TGS_ListTable destTable) {
        return fill(executor, destTable, false, null);
    }

    public static TGS_UnionExcuseVoid fill(TS_SQLSelectExecutor executor, TGS_ListTable destTable, boolean addHeaders) {
        return fill(executor, destTable, addHeaders, null);
    }

    public static TGS_UnionExcuseVoid fill(TS_SQLSelectExecutor executor, TGS_ListTable destTable, List<String> useOptionalHeaders) {
        return fill(executor, destTable, false, useOptionalHeaders);
    }

    private static TGS_UnionExcuseVoid fill(TS_SQLSelectExecutor executor, TGS_ListTable destTable, boolean addHeaders, List<String> useOptionalHeaders) {
        var wrap = new Object() {
            TGS_UnionExcuseVoid u_fill = null;
        };
        var u_walk = executor.walk(rs -> {
            wrap.u_fill = fill(rs, destTable, addHeaders, useOptionalHeaders);
        }, rs -> {
            wrap.u_fill = fill(rs, destTable, addHeaders, useOptionalHeaders);
        });
        if (wrap.u_fill != null && wrap.u_fill.isExcuse()) {
            return wrap.u_fill;
        }
        return u_walk;
    }

    public static TGS_UnionExcuseVoid fill(TS_SQLResultSet rs, TGS_ListTable destTable) {
        return fill(rs, destTable, false, null);
    }

    public static TGS_UnionExcuseVoid fill(TS_SQLResultSet rs, TGS_ListTable destTable, boolean addHeaders) {
        return fill(rs, destTable, addHeaders, null);
    }

    public static TGS_UnionExcuseVoid fill(TS_SQLResultSet rs, TGS_ListTable destTable, List<String> useOptionalHeaders) {
        return fill(rs, destTable, false, useOptionalHeaders);
    }

    private static TGS_UnionExcuseVoid fill(TS_SQLResultSet rs, TGS_ListTable destTable, boolean addHeaders, List<String> useOptionalHeaders) {
        destTable.clear();
        var wrap = new Object() {
            TGS_UnionExcuse<String> u_rs_str_get = null;
        };
        var u_walk = rs.walkCells(rs0 -> d.ci("fill", "empty", rs.meta.command()), (ri, ci) -> {
            if (wrap.u_rs_str_get != null && wrap.u_rs_str_get.isExcuse()) {
                return;
            }
            wrap.u_rs_str_get = rs.str.get(ri, ci);
            if (wrap.u_rs_str_get.isExcuse()) {
                return;
            }
            destTable.setValue(ri, ci, wrap.u_rs_str_get.value());
        });
        if (wrap.u_rs_str_get.isExcuse()) {
            return wrap.u_rs_str_get.toExcuseVoid();
        }
        if (u_walk.isExcuse()) {
            return u_walk;
        }
        if (useOptionalHeaders != null) {
            addHeaders = true;
        }
        if (!addHeaders) {
            return TGS_UnionExcuseVoid.ofVoid();
        }
        destTable.insertEmptyRow(0);
        var u_colSize = rs.col.size();
        if (u_colSize.isExcuse()) {
            return u_colSize.toExcuseVoid();
        }
        for (var ci = 0; ci < u_colSize.value(); ci++) {
            var u = rs.col.name(ci);
            if (u.isExcuse()) {
                return u.toExcuseVoid();
            }
            destTable.setValue(0, ci, u.value());
        }
        if (useOptionalHeaders == null) {
            return TGS_UnionExcuseVoid.ofVoid();
        }
        IntStream.range(0, useOptionalHeaders.size()).forEachOrdered(ci -> {
            destTable.setValue(0, ci, useOptionalHeaders.get(ci));
        });
        return TGS_UnionExcuseVoid.ofVoid();
    }
}
