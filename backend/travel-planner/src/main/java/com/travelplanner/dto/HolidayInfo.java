package com.travelplanner.dto;

import lombok.Data;

@Data
public class HolidayInfo {
    private String date;
    private Boolean isHoliday;
    private String holidayName;
    private Boolean isWorkday;
    private Integer wage;
}
