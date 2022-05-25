package json;

public class Statistics extends Deserializer<StatisticsStruct> {
    /**
     * Constructor for Statistics class.
     */
    public Statistics() {
        super(StatisticsStruct.class, "statistics.json");
    }
}
