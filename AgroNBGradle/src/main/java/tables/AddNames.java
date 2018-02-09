package tables;

public class AddNames {

    private String group;
    private int groupId;
    private String[] groupNames;
    private double priceIn;
    private double priceOut;

    public AddNames(){}

    public AddNames(String group, int groupId, String[] groupNames, double priceIn, double priceOut){
        this.group = group;
        this.groupId = groupId;
        this.groupNames = groupNames;
        this.priceIn = priceIn;
        this.priceOut = priceOut;
    }

    public String getGroup() {
        return group;
    }

    public String[] getGroupNames() {
        return groupNames;
    }

    public int getGroupId() {
        return groupId;
    }

    public double getPriceIn() {
        return priceIn;
    }

    public double getPriceOut() {
        return priceOut;
    }



}
