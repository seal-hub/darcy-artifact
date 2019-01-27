module giselle.db {
    requires java.sql;
    requires sqlite.jdbc;
    requires transitive giselle.common;

    exports giselle.db;

}