package com.travelplanner.data.local.entity;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "itinerary_days", foreignKeys = @ForeignKey(entity = ItineraryEntity.class, parentColumns = "id", childColumns = "itineraryId", onDelete = ForeignKey.CASCADE), indices = {
                @Index(value = "itineraryId") })
public class ItineraryDayEntity {
        @PrimaryKey(autoGenerate = true)
        private Long id;
        private Long itineraryId;
        private Integer dayNumber;
        private String date;
        private String notes;
        private Long createTime;
        private Long updateTime;

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public Long getItineraryId() {
                return itineraryId;
        }

        public void setItineraryId(Long itineraryId) {
                this.itineraryId = itineraryId;
        }

        public Integer getDayNumber() {
                return dayNumber;
        }

        public void setDayNumber(Integer dayNumber) {
                this.dayNumber = dayNumber;
        }

        public String getDate() {
                return date;
        }

        public void setDate(String date) {
                this.date = date;
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