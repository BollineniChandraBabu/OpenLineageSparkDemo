package org.example;

import io.openlineage.spark.agent.OpenLineageSparkListener;
import java.util.Collections;
import java.util.List;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

public class Main {
    static SparkSession spark;

    public static void main(String[] args) {
        System.out.printf("Hello and welcome!");
        System.setProperty("hadoop.home.dir", "D:\\chandra\\");
        spark = SparkSession.builder().master("local[1]").appName("IcebergIntegrationTest")
                .config("spark.driver.host", "127.0.0.1")
                .config("spark.driver.bindAddress", "127.0.0.1")
                .config("spark.sql.shuffle.partitions", 1L)
                .config("spark.sql.warehouse.dir", "D:\\chandra\\tmp\\iceberg")
                .config("spark.driver.extraJavaOptions", "-Dderby.system.home=/tmp/iceberg")
                .config("spark.openlineage.transport.type", "http")
                .config("spark.openlineage.transport.url",
                        "http://ip172-18-0-39-chfnm781k7jg00a3poc0-5000.direct.labs.play-with-docker.com/api/v1/namespaces/iceberg-namespace").config("spark.openlineage.facets.disabled", "spark_unknown;spark.logicalPlan").config("spark.extraListeners", OpenLineageSparkListener.class.getName()).config("spark.sql.catalog.spark_catalog", "org.apache.iceberg.spark.SparkCatalog").config("spark.sql.catalog.spark_catalog.type", "hadoop").config("spark.sql.catalog.spark_catalog.warehouse", "/tmp/iceberg").config("spark.sql.extensions", "org.apache.iceberg.spark.extensions.IcebergSparkSessionExtensions").config("spark.sql.sources.partitionOverwriteMode", "dynamic").getOrCreate();
        Dataset df = spark.read().text("D:\\chandra\\code\\OpenLineageSparkDemo\\src\\main\\resources\\data.txt");
        Dataset agg = df.groupBy("value", new String[0]).count();
        agg.write().mode("overwrite").csv("D:\\chandra\\code\\OpenLineageSparkDemo\\src\\main\\java\\org\\csv");
        spark.sparkContext().setLogLevel("WARN");
        clearTables(Collections.singletonList("alter_table_test"), spark);
        spark.sql("CREATE TABLE alter_table_test (a string, b string) USING iceberg");
        spark.sql("INSERT INTO alter_table_test VALUES ('a', 'b')");
        spark.sql("ALTER TABLE alter_table_test RENAME COLUMN b TO c");
        spark.stop();
    }

    private static void clearTables(List<String> tables, SparkSession spark) {
        tables.stream().filter((t) -> {
            return spark.catalog().tableExists(t);
        }).forEach((t) -> {
            spark.sql("DROP TABLE IF EXISTS " + t);
        });
    }
}
