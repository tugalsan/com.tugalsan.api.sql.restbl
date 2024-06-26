module com.tugalsan.api.sql.restbl {
    
    requires com.tugalsan.api.unsafe;
    requires com.tugalsan.api.cast;
    requires com.tugalsan.api.list;
    requires com.tugalsan.api.time;
    requires com.tugalsan.api.stream;
    requires com.tugalsan.api.log;
    requires com.tugalsan.api.callable;
    requires com.tugalsan.api.string;
    
    requires com.tugalsan.api.sql.resultset;
    requires com.tugalsan.api.sql.select;
    exports com.tugalsan.api.sql.restbl.server;
}
