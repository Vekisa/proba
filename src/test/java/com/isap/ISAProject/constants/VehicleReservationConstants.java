package com.isap.ISAProject.constants;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class VehicleReservationConstants {
	public static final int PAGE_SIZE = 3;
	public static final Long DB_ID = 1L;
    public static final Date DB_BEGIN_DATE = Date.from(LocalDateTime.parse("2018-09-16T08:00:00").toInstant(ZoneOffset.UTC));
    public static final Date DB_END_DATE = Date.from(LocalDateTime.parse("2018-09-16T20:00:00").toInstant(ZoneOffset.UTC));
    public static final double DB_PRICE = 100.00;
    
    public static final Long NEW_ID = 2L;
    public static final Date NEW_BEGIN_DATE = Date.from(LocalDateTime.parse("2018-03-16T07:00:00").toInstant(ZoneOffset.UTC));
    public static final Date NEW_END_DATE = Date.from(LocalDateTime.parse("2018-03-17T09:00:00").toInstant(ZoneOffset.UTC));
    public static final double NEW_PRICE = 140.00;
    
    public static final Long DB_ID_TO_DELETE = 3L;
}
