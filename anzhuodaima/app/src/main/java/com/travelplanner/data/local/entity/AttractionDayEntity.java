package com.travelplanner.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "attraction_days", foreignKeys = {
                @ForeignKey(entity = ItineraryDayEntity.class, parentColumns = "id", childColumns = "dayId", onDelete = ForeignKey.CASCADE),
                @ForeignKey(entity = AttractionEntity.class, parentColumns = "id", childColumns = "attractionId", onDelete = ForeignKey.CASCADE)
}, indices = { @Index(value = "dayId"), @Index(value = "attractionId") })
public class AttractionDayEntity {
        @PrimaryKey(autoGenerate = true)
        private Long id;
        private Long dayId;
        private Long attractionId;
        private Integer visitOrder;
        private String plannedStartTime;
        private String plannedEndTime;
        private String notes;
        private Long createTime;
        private Long updateTime;

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public Long getDayId() {
                return dayId;
        }

        public void setDayId(Long dayId) {
                this.dayId = dayId;
        }

        public Long getAttractionId() {
                return attractionId;
        }

        public void setAttractionId(Long attractionId) {
                this.attractionId = attractionId;
        }

        public Integer getVisitOrder() {
                return visitOrder;
        }

        public void setVisitOrder(Integer visitOrder) {
                this.visitOrder = visitOrder;
        }

        public String getPlannedStartTime() {
                return plannedStartTime;
        }

        public void setPlannedStartTime(String plannedStartTime) {
                this.plannedStartTime = plannedStartTime;
        }

        public String getPlannedEndTime() {
                return plannedEndTime;
        }

        public void setPlannedEndTime(String plannedEndTime) {
                this.plannedEndTime = plannedEndTime;
        }

        public String getNotes() {
                return notes;
        }

        public void setNotes(String notes) {
                this.notes = notes;
        }

        public Long getCreateTime() {
                return createTime;
        }

        public void setCreateTime(Long createTime) {
                this.createTime = createTime;
        }

        public Long getUpdateTime() {
                return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
                this.updateTime = updateTime;
        }
}