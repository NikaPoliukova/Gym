package upskill.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Month;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthData {
  private Month month;
  private int trainingsSummaryDuration;
}

