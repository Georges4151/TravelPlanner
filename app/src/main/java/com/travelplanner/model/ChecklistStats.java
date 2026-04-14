package com.travelplanner.model;

import java.util.Map;

public class ChecklistStats {
    private Integer total;
    private Double progress;
    private Integer unpacked;
    private Integer packed;

    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
    public Double getProgress() { return progress; }
    public void setProgress(Double progress) { this.progress = progress; }
    public Integer getUnpacked() { return unpacked; }
    public void setUnpacked(Integer unpacked) { this.unpacked = unpacked; }
    public Integer getPacked() { return packed; }
    public void setPacked(Integer packed) { this.packed = packed; }
}
