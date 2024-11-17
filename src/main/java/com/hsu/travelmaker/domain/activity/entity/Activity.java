package com.hsu.travelmaker.domain.activity.entity;

import com.hsu.travelmaker.domain.schedule.entity.Schedule;
import com.hsu.travelmaker.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "ACTIVITY")
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Activity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_id")
    private Long activityId; // 활동 PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule; // 일정 FK

    @Column(name = "activity_time", nullable = false)
    private String activityTime; // 활동 시간

    @Column(name = "activity_title", nullable = false)
    private String activityTitle; // 활동 제목

    @Column(name = "activity_content", nullable = false)
    private String activityContent; // 활동 내용

    @Column(name = "activity_expense", nullable = false)
    private BigDecimal activityExpense; // 활동 비용

}
