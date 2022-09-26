package com.example.flink14.analysis.attendance;

import com.example.flink14.analysis.utils.FlinkStreamUtil;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.api.TableDescriptor;
import org.apache.flink.table.api.TableResult;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;
import org.apache.flink.types.RowKind;
import org.apache.flink.util.CloseableIterator;

import java.util.HashMap;
import java.util.Map;

public class MonthAttendStatistic_mybatis {

    public static void main(String[] args) throws Exception {

        try {
            StreamTableEnvironment tableEnvironment = FlinkStreamUtil.buildTableEnvironment();

            String sourceDDL =
                    "CREATE TABLE ts_day_attend_fix (\n" +
                            " id BIGINT NOT NULL,\n" +
                            " TENANTID STRING,\n" +
                            " scheme_id STRING,\n" +
                            " PRIMARY KEY (id) NOT ENFORCED \n" +
                            ") WITH (\n" +
                            " 'connector' = 'mysql-cdc' ,\n" +
                            " 'hostname' = '172.20.47.57' ,\n" +
                            " 'port' = '8306' ,\n" +
                            " 'username' = 'ro_all_db' ,\n" +
                            " 'password' = '7hFYJo47kW6PYPAEbG75Lzvk' ,\n" +
                            " 'database-name' = 'hr_dataanalysis' ,\n" +
                            " 'scan.incremental.snapshot.enabled' = 'false' ,\n" +
                            " 'scan.startup.mode' = 'initial' ,\n" +
                            " 'table-name' = 'hr_dataanalysis.ts_day_attend_fix' \n" +
                            ")";

            TableResult tableResult = tableEnvironment.executeSql(sourceDDL);

            //tableEnvironment.from("ts_day_attend_fix").select("id").execute().print();


          /*  CloseableIterator<Row> collect1 =
                    tableEnvironment.sqlQuery("select id,TENANTID,scheme_id from ts_day_attend_fix").execute().collect();

            while (collect1.hasNext()) {
                Row next = collect1.next();
                System.out.println(next);
            }
*/
            Map<String, String> sourceCols = new HashMap<>();
            sourceCols.put("id", "bigint");
            sourceCols.put("TENANTID", "varchar");
            //sourceCols.put("scheme_id", "varchar");
            Schema sourceSchema = FlinkStreamUtil.buildSchema(sourceCols, "sourceTable");
            TableDescriptor sourceTable = FlinkStreamUtil.buildMysqlCdcTable("mysql-cdc", "172.20.47.57",
                    "8306", "ro_all_db", "7hFYJo47kW6PYPAEbG75Lzvk",
                    "hr_dataanalysis", "hr_dataanalysis.ts_day_attend_fix", sourceSchema);

//            TableDescriptor sourceTable = FlinkStreamUtil.buildMysqlCdcTable("mysql-cdc", "localhost",
//                    "3306", "root", "root",
//                    "hrattenddb", "ts_monthstat", sourceSchema);

            tableEnvironment.createTemporaryTable("ts_day_attend_fix2", sourceTable);
            tableEnvironment.from("ts_day_attend_fix2").select("id").execute().print();

            CloseableIterator<Row> collect = tableEnvironment.from(sourceTable).execute().collect();

            while (collect.hasNext()) {
                Row next = collect.next();
                RowKind kind = next.getKind();
                String id = next.getField("ID").toString();
                String TENANTID = next.getField("TENANTID").toString();
                Object SCHEME_ID = next.getField("SCHEME_ID");

                if (RowKind.INSERT.equals(kind)) {
                    // todo insert

                } else if (RowKind.UPDATE_BEFORE.equals(kind)) {
                    // todo insert or update

                } else if (RowKind.UPDATE_AFTER.equals(kind)) {
                    // todo  update

                } else if (RowKind.DELETE.equals(kind)) {
                    // todo delete

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
