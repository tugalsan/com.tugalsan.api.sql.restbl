package com.tugalsan.api.sql.restbl.server;

import com.tugalsan.api.log.server.*;
import com.tugalsan.api.string.client.*;
import com.tugalsan.api.union.client.TGS_UnionExcuse;

public class TS_SQLResTblValue {

    final private static TS_Log d = TS_Log.of(TS_SQLResTblValue.class);

    public String tableAndColumnName;
    public String id;
    public String prefix;
    public String suffix;

    @Override
    public String toString() {
        return TGS_StringUtils.concat(TS_SQLResTblValue.class.getSimpleName(), ":{tableAndColumnName=[", tableAndColumnName, "], id=", String.valueOf(id), ", prefix=[", prefix, "], suffix=[", suffix, "]");
    }

    public TGS_UnionExcuse<String> getTableName() {
        var first_ = tableAndColumnName.indexOf('.');
        if (first_ == -1) {
            TGS_UnionExcuse.ofExcuse(d.className, "getTableName", "tableAndColumnName:[" + tableAndColumnName + "].first_.indexOf('.') == -1");
        }
        var tableName = tableAndColumnName.substring(0, first_);
        d.ci("getTableName->.tableAndColumnName", tableAndColumnName, "first_", first_, "tableName as tableAndColumnName.substring(0, first_)", tableName);
        return TGS_UnionExcuse.of(tableName);
    }

    public TGS_UnionExcuse<String> getColumnName() {
        var u_tn = getTableName();
        if (u_tn.isExcuse()){
            return u_tn;
        }
        return TGS_UnionExcuse.of(tableAndColumnName.substring(u_tn.value().length() + 1));
    }

    public TGS_UnionExcuse<String> getLinkedTableName() {
        var u_cn = getColumnName();
        if (u_cn.isExcuse()){
            return u_cn;
        }
        var cn = u_cn.value();
        int first_, second_;
        {
            first_ = cn.indexOf('_');
            if (first_ == -1) {
                TGS_UnionExcuse.ofExcuse(d.className, "getLinkedTableName", "tableAndColumnName:[" + tableAndColumnName + "]&rest:[" + cn + "].first_.indexOf('_') == -1");
            }
            second_ = cn.indexOf('_', first_ + 1);
            if (second_ == -1) {
                TGS_UnionExcuse.ofExcuse(d.className, "getLinkedTableName", "tableAndColumnName:[" + tableAndColumnName + "]&rest:[" + cn + "].second_.indexOf('_') == -1");
            }
        }
        return TGS_UnionExcuse.of(cn.substring(first_ + 1, second_));
    }

    public TGS_UnionExcuse<String> getLinkedColName() {
        var u_cn = getColumnName();
        if (u_cn.isExcuse()){
            return u_cn;
        }
        var cn = u_cn.value();
        int first_, second_;
        {
            first_ = cn.indexOf('_');
            if (first_ == -1) {
                TGS_UnionExcuse.ofExcuse(d.className, "getLinkedColName", "tableAndColumnName:[" + tableAndColumnName + "]&rest:[" + cn + "].first_.indexOf('_') == -1");
            }
            second_ = cn.indexOf('_', first_ + 1);
            if (second_ == -1) {
                TGS_UnionExcuse.ofExcuse(d.className, "getLinkedColName", "tableAndColumnName:[" + tableAndColumnName + "]&rest:[" + cn + "].second_.indexOf('_') == -1");
            }
        }
        return TGS_UnionExcuse.of(cn.substring(second_ + 1));
    }

    public TS_SQLResTblValue(String tableAndColumnName, String id) {
        this(tableAndColumnName, id, "", "");
    }

    public TS_SQLResTblValue(String tableAndColumnName, String id, String prefix, String suffix) {
        this.tableAndColumnName = tableAndColumnName;
        this.id = id;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public TS_SQLResTblValue cloneIt() {
        return new TS_SQLResTblValue(tableAndColumnName, id, prefix, suffix);
    }
}
