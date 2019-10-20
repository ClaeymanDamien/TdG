package read_and_process_graph;

import java.util.ArrayList;
import java.util.List;

public class L3_A7_Summit implements Cloneable {

    private Integer summit;
    private Integer weight;
    private List<Integer> originSummit;
    private Boolean successor;

    public L3_A7_Summit(int summit, int weight, int originSummit, boolean successor) {
        this.summit = summit;
        this.weight = weight;
        this.originSummit = new ArrayList<Integer>();
        this.originSummit.add(originSummit);
        this.successor = successor;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((originSummit == null) ? 0 : originSummit.hashCode());
        result = prime * result + ((summit == null) ? 0 : summit.hashCode());
        result = prime * result + ((weight == null) ? 0 : weight.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        L3_A7_Summit other = (L3_A7_Summit) obj;
        if (originSummit == null) {
            if (other.originSummit != null)
                return false;
        } else if (!originSummit.equals(other.originSummit))
            return false;
        if (summit == null) {
            if (other.summit != null)
                return false;
        } else if (!summit.equals(other.summit))
            return false;
        if (weight == null) {
            if (other.weight != null)
                return false;
        } else if (!weight.equals(other.weight))
            return false;
        return true;
    }

    public L3_A7_Summit(int summit, int weight, List<Integer> originSummit, boolean successor) {
        this.summit = summit;
        this.weight = weight;
        this.originSummit = new ArrayList<Integer>();
        for(Integer value : originSummit) {
            this.originSummit.add(value);
        }
        this.successor = successor;
    }

    public L3_A7_Summit()
    {
        summit = null;
        weight = null;
        originSummit = null;
    }

    public L3_A7_Summit(int summit, boolean successor) {
        this.summit = summit;
        this.weight = null;
        this.originSummit = null;
        this.successor = successor;
    }

    public Object clone(){
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }
    /**
     * Getters
     */

    public Integer getSummit() {
        return summit;
    }

    public Integer getWeight() {
        return weight;
    }

    public List<Integer> getOriginSummit() {
        return originSummit;
    }

    public Boolean getSuccessor() {
        return successor;
    }

    /**
     * Setters
     */

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setOriginSummit(List<Integer> originSummit) {
        this.originSummit = originSummit;
    }

    public void setSuccessor(Boolean successor) {
        this.successor = successor;
    }

    public String printOriginSummit() {
        String output="(";
        Boolean first = true;
        for(Integer summit : originSummit) {
            if(first) {
                output += summit;
                first = false;
            }
            else {
                output += ","+summit;
            }
        }

        return output += ")";
    }

    @Override
    public String toString() {
        String output;
        output = originSummit+" -> " + weight + " -> " + summit +"\n";
        output += "successor: "+ successor +"\n";
        return output;
    }
}