package com.yoshi.sleep_log.domain.repository;

import com.yoshi.sleep_log.domain.model.SleepEvent;
import com.yoshi.sleep_log.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SleepEventRepository extends JpaRepository<SleepEvent, Long> {

    // 特定ユーザーの睡眠イベントを発生時刻の昇順で取得する
    List<SleepEvent> findByUserOrderByEventTimeAsc(User user);

    // 特定ユーザーの指定期間内の睡眠イベントを発生時刻の昇順で取得する
    List<SleepEvent> findByUserAndEventTimeBetweenOrderByEventTimeAsc(
            User user, LocalDateTime from, LocalDateTime to);
}
