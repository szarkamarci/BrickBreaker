package json;

public class Statistics extends Deserializer<StatisticsStruct> {
    /**
     * Searching for json files
     */
    public Statistics() {
        super(StatisticsStruct.class, "statistics.json");
    }
}
