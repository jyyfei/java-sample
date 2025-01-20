package com.java.sample.interview.ae.email;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@Data
public class Email {
    private Integer sender;
    private String title;
    private LocalDateTime time;

    public boolean choice(String titleLike, Integer sender, LocalDateTime start, LocalDateTime end) {
        Boolean timeAfter = timeAfter(start, this.getTime());
        Boolean timeBefore = timeBefore(end, this.getTime());
        Boolean senderGood = senderGood(sender, this.getSender());
        Boolean titleGood = titleGood(titleLike, this.getTitle());
        return timeAfter && timeBefore && senderGood && titleGood;
    }

    private Boolean timeAfter(LocalDateTime start, LocalDateTime emailTime) {
        if (Objects.isNull(start)) {
            return true;
        }
        return emailTime.isAfter(start);
    }

    private Boolean timeBefore(LocalDateTime end, LocalDateTime emailTime) {
        if (Objects.isNull(end)) {
            return true;
        }
        return emailTime.isBefore(end);
    }

    private Boolean senderGood(Integer sender, Integer emailSender) {
        if (Objects.isNull(sender)) {
            return true;
        }
        return emailSender.equals(sender);
    }

    private Boolean titleGood(String titleLike, String emailTitle) {
        if (Objects.isNull(titleLike) || titleLike.isEmpty()) {
            return true;
        }
        return emailTitle.contains(titleLike);
    }
}
