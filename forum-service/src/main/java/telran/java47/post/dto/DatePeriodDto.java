package telran.java47.post.dto;

import java.time.LocalDate;

import org.bson.codecs.jsr310.LocalDateTimeCodec;

import lombok.Getter;

@Getter
public class DatePeriodDto {
	LocalDate dateFrom;
	LocalDate dateTo;
}
