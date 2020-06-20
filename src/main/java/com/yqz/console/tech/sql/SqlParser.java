package com.yqz.console.tech.sql;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SqlParser {

    public static void main(String[] args) {
        String sql = "select  a.* from ActivityInfo a ";
        isSingleStatementWithinSingleTable(sql, JdbcConstants.SQL_SERVER_DRIVER);
    }

    public static boolean isSingleStatementWithinSingleTable(String sql, String dbType) {
        if (null == sql || "" == sql.trim()) {
            System.out.println("sql is null or blank");
            return false;
        }


        try {
            List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
            if (stmtList.size() > 1) {
                System.out.println("multiple sql statement: " + stmtList.stream().map(p -> p.toLowerCaseString()).collect(Collectors.joining(System.lineSeparator())));
                return false;
            }
            SQLStatement stmt = stmtList.get(0);
            SQLServerSchemaStatVisitor visitor = new SQLServerSchemaStatVisitor();
            stmt.accept(visitor);
            if (visitor.getTables().size() > 1) {
                System.out.println("multiple table: " + stmtList.get(0));
                return false;
            }

            String stmtStr = stmtList.get(0).toString();

            Map.Entry<TableStat.Name, TableStat> entry = visitor.getTables().entrySet().iterator().next();
            if (!"select".equalsIgnoreCase(entry.getValue().toString())) {
                System.out.println("illegal table operation: " + entry.getValue().toString());
                return false;
            }

            String illegalStr = null;
            if (stmtStr.contains("--"))
                illegalStr = "--";
            else if (stmtStr.contains("#"))
                illegalStr = "#";


            if (illegalStr != null) {
                System.out.println(String.format("illegal character %s in statement %s", stmtStr));
                return false;
            }
        } catch (Exception e) {
            System.out.println("isSingleStatementWithinSingleTable error. " + e);
            return false;
        }

        return true;

    }

    private void test() {
        // String sql = "update t set name = 'x' where id < 100 limit 10";
        // String sql = "SELECT ID, NAME, AGE FROM USER WHERE ID = ? limit 2";
        // String sql = "select * from tablename limit 10";

        String sql = "select top(100) sex,age from emp_table join table2 on a=b where area='beijing' and id>100 group by sex,age;update user set name='jack' where id=1";
        String dbType = JdbcConstants.SQL_SERVER_DRIVER;

        //格式化输出
        String result = SQLUtils.format(sql, dbType);
        System.out.println(result); // 缺省大写格式
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        //解析出的独立语句的个数
        System.out.println("size is:" + stmtList.size());
        for (int i = 0; i < stmtList.size(); i++) {

            SQLStatement stmt = stmtList.get(i);
            SQLServerSchemaStatVisitor visitor = new SQLServerSchemaStatVisitor();
            stmt.accept(visitor);

            //获取表名称
//            System.out.println("Tables : " + visitor.get());
            //获取操作方法名称,依赖于表名称
            System.out.println("Manipulation : " + visitor.getTables());
            //获取字段名称
            System.out.println("fields : " + visitor.getColumns());
            System.out.println(visitor);

            System.out.println("------------------------------------------ ");
        }
    }


}

